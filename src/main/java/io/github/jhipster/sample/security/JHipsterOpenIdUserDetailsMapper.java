package io.github.jhipster.sample.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.configuration.OpenIdAdditionalClaimsConfiguration;
import io.micronaut.security.oauth2.endpoint.token.response.*;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

@Singleton
@Requires(configuration = "io.micronaut.security.token.jwt")
@Named("oidc")
public class JHipsterOpenIdUserDetailsMapper extends DefaultOpenIdUserDetailsMapper {

    /**
     * Default constructor.
     *
     * @param openIdAdditionalClaimsConfiguration The additional claims configuration
     */
    public JHipsterOpenIdUserDetailsMapper(OpenIdAdditionalClaimsConfiguration openIdAdditionalClaimsConfiguration) {
        super(openIdAdditionalClaimsConfiguration);
    }


    @Override
    @Nonnull
    public UserDetails createUserDetails(String providerName, OpenIdTokenResponse tokenResponse, OpenIdClaims openIdClaims) {
        Map<String, Object> claims = buildAttributes(providerName, tokenResponse, openIdClaims);
        List<String> roles = getRoles(providerName, tokenResponse, openIdClaims);
        String username = getUsername(providerName, tokenResponse, openIdClaims);
        return new UserDetails(username, roles, claims);
    }

//    public static List<String> extractAuthorityFromClaims(OpenIdClaims openIdClaims) {
//        return mapRolesToGrantedAuthorities(getRolesFromClaims(openIdClaims));
//    }
//
//    @SuppressWarnings("unchecked")
//    private static Collection<String> getRolesFromClaims(OpenIdClaims claims) {
//        return (Collection<String>) claims."groups",
//            claims.getOrDefault("roles", new ArrayList<>()));
//    }
//
//    private static List<String> mapRolesToGrantedAuthorities(Collection<String> roles) {
//        return roles.stream()
//            .filter(role -> role.startsWith("ROLE_"))
//            .map(SimpleGrantedAuthority::new)
//            .collect(Collectors.toList());
//    }

    /**
     * @param providerName The OpenID provider name
     * @param tokenResponse The token response
     * @param openIdClaims The OpenID claims
     * @return The roles to set in the {@link UserDetails}
     */
    @Override
    protected List<String> getRoles(String providerName, OpenIdTokenResponse tokenResponse, OpenIdClaims openIdClaims) {

        System.out.println(openIdClaims.getClaims());

        return Collections.emptyList();
    }


    /**
     * @param providerName The OpenID provider name
     * @param tokenResponse The token response
     * @param openIdClaims The OpenID claims
     * @return The username to set in the {@link UserDetails}
     */
    protected String getUsername(String providerName, OpenIdTokenResponse tokenResponse, OpenIdClaims openIdClaims) {
        return openIdClaims.getPreferredUsername();
    }

}
