package ivanitsya.security;

import ivanitsya.users.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Администратор on 13.07.2016.
 */

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    public TokenAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        String token = (String) tokenAuthentication.getPrincipal();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(token.split(":")[0]);// TODO: 13.07.2016 загрузка должна быть с базы по идее, хотя каждый раз с базы . надо погуглить
        UserDAO userDetails = (UserDAO) userDetailsService.loadUserByUsername(token.split(":")[0]);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Unknown token");
        }

        tokenAuthentication.setAuthenticated(true);
        tokenAuthentication.setDetails(userDetails);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TokenAuthentication.class;
    }

}

