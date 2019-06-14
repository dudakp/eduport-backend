package sk.dudak.eduport.configuration;

public class SecurityConstants {
    public static final String AUTH_LOGIN_URL = "/v1/authenticate";

    // Signing key for HS512 algorithm
    public static final String JWT_SECRET = "*G-KaNdRgUkXp2s5v8y/B?E(H+MbQeShVmYq3t6w9z$C&F)J@NcRfUjXnZr4u7x!";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
}
