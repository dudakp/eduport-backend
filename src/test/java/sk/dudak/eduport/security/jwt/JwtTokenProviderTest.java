package sk.dudak.eduport.security.jwt;

import io.jsonwebtoken.JwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import sk.dudak.eduport.model.user.User;

import java.util.ArrayList;

public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private String validMockToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6InN0dWRlbnQiLCJleHAiOjE1NjEzMTI4MDIsInJvbGVzIjpbIlJPTEVfU1RVREVOVCJdLCJ1c2VybmFtZSI6InN0dWRlbnQifQ.oTPZGtnyLXTcAy4whfC9pTs6eYBLK_935Bci8Ar1pfxnS6GRqKK4pXtNvwPL8Y-LyV5pHoTK6W88JYn5XL0Yiw";
    private String nonValidMockToken = "Bearer token.nonvalid.abc";

    @Before
    public void setUp() {
        this.jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        this.user = User.builder()
                .id(4)
                .username("student")
                .password("student")
                .roles(new ArrayList<String>() {{
                    add("ROLE_STUDENT");
                }})
                .build();
    }

    @Test
    public void createTokenForNonExistentUser() throws NonExistentUsernameException {
        Mockito.when(this.jwtTokenProvider.createToken("", this.user.getRoles())).thenThrow(NonExistentUsernameException.class);

        Assertions.assertThrows(NonExistentUsernameException.class, () -> this.jwtTokenProvider.createToken("", this.user.getRoles()));
    }

    @Test
    public void validateValidToken() {
        Mockito.when(this.jwtTokenProvider.validateToken(this.validMockToken)).thenThrow(JwtException.class);

        Assertions.assertThrows(JwtException.class, () -> this.jwtTokenProvider.validateToken(this.validMockToken));
    }

    @Test
    public void validateNonValidToken() {
        Mockito.when(this.jwtTokenProvider.validateToken(this.nonValidMockToken)).thenReturn(false);

        Assertions.assertFalse(this.jwtTokenProvider.validateToken(this.nonValidMockToken));
    }
}
