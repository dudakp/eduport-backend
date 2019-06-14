package sk.dudak.eduport.controller.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.dudak.eduport.dao.course.UserRepository;
import sk.dudak.eduport.model.security.AuthenticationRequest;
import sk.dudak.eduport.model.security.RegisterForm;
import sk.dudak.eduport.model.user.User;
import sk.dudak.eduport.security.jwt.JwtTokenProvider;
import sk.dudak.eduport.security.jwt.NonExistentUsernameException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) throws NonExistentUsernameException {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization",
                    "Bearer " + model.get("token"));

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping
    @RequestMapping(value = "/register")
    public ResponseEntity registerUser(@RequestBody RegisterForm registeree) {
        final User toRegister = this.userRepository.save(User.builder()
                .username(registeree.getUsername())
                .password(this.passwordEncoder.encode(registeree.getPassword()))
                .roles(Arrays.asList(registeree.getRoles()))
                .build()
        );
        return ok(toRegister);
    }
}
