package sk.dudak.eduport.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import sk.dudak.eduport.configuration.SecurityConstants;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.user.User;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    //    @Valu e("${security.jwt.secret:JwtSecretKey}")
    private String secretKey = "*G-KaNdRgUkXp2s5v8y/B?E(H+MbQeShVmYq3t6w9z$C&F)J@NcRfUjXnZr4u7x!";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    private String username;

    public JwtTokenProvider(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles) throws NonExistentUsernameException {

        Claims claims = Jwts.claims()
                .setSubject(username);

        final Optional<User> user = this.userRepository.findByUsername(username);

        if (user.isPresent()) {
            claims.put("roles", user.get().getRoles());
            claims.put("username", user.get().getUsername());

            Date now = new Date();
            Date validity = new Date(now.getTime() + validityInMilliseconds);
            final byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();


            return Jwts.builder()
                    .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                    .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                    .setIssuer(SecurityConstants.TOKEN_ISSUER)
                    .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                    .claim("roles", roles)
                    .claim("username", username)
                    .compact();
        } else
            throw new NonExistentUsernameException("User with username: " + username + "does not exists!");
    }

    public Authentication getAuthentication(String token) {
//        final String substring = token.substring(7);
        final String username = this.getUsername();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername() {
        return this.username;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.secretKey.getBytes())
                    .parseClaimsJws(token);
            this.username = claims.getBody().getSubject();
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}
