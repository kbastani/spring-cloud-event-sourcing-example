package demo.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientDetailsService clients;

    @Autowired
    private HttpSessionSecurityContextRepository sessionRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpRequestResponseHolder responseHolder = new HttpRequestResponseHolder(request, response);
        sessionRepository.loadContext(responseHolder);

        try {
            // Authenticate the user with the supplied credentials
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(request.getParameter("username"),
                            request.getParameter("password"), authorities);

            SecurityContextHolder.getContext()
                    .setAuthentication(authenticationManager.authenticate(auth));

            // Authenticate the user
            if(!authenticationManager.authenticate(auth).isAuthenticated())
                throw new CredentialException("User could not be authenticated");

        } catch (Exception ex) {
            // The user couldn't be authenticated, redirect back to login
            ex.printStackTrace();
            return "login";
        }

        // Get the default saved request from session
        DefaultSavedRequest defaultSavedRequest = ((DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST"));

        // Generate an authorization parameter map for the token request
        Map<String, String> authParams = getAuthParameters(defaultSavedRequest);

        // Create the authorization request and put it in the view model
        AuthorizationRequest authRequest = new DefaultOAuth2RequestFactory(clients).createAuthorizationRequest(authParams);
        authRequest.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
        sessionRepository.saveContext(SecurityContextHolder.getContext(), responseHolder.getRequest(), responseHolder.getResponse());
        model.addAttribute("authorizationRequest", authRequest);

        // Return the token authorization view
        return "authorize";
    }

    /**
     * Generate an authorization parameter map from the session's token request
     * @param defaultSavedRequest the default saved request from the session
     * @return a map of parameters containing the OAuth2 request details
     */
    private Map<String, String> getAuthParameters(DefaultSavedRequest defaultSavedRequest) {
        Map<String, String> authParams = new HashMap<>();

        authParams.put(OAuth2Utils.CLIENT_ID,
                defaultSavedRequest.getParameterMap().get(OAuth2Utils.CLIENT_ID)[0]);

        authParams.put(OAuth2Utils.REDIRECT_URI,
                defaultSavedRequest.getParameterMap().get(OAuth2Utils.REDIRECT_URI)[0]);

        if(defaultSavedRequest.getParameterMap().get(OAuth2Utils.STATE) != null) {
            authParams.put(OAuth2Utils.STATE,
                    defaultSavedRequest.getParameterMap().get(OAuth2Utils.STATE)[0]);
        }

        authParams.put(OAuth2Utils.RESPONSE_TYPE, "code");
        authParams.put(OAuth2Utils.USER_OAUTH_APPROVAL, "true");
        authParams.put(OAuth2Utils.GRANT_TYPE, "authorization_code");
        return authParams;
    }
}
