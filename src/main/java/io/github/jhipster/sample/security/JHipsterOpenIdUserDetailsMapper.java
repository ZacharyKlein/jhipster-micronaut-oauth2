package io.github.jhipster.sample.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.context.annotation.Requires;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.configuration.OpenIdAdditionalClaimsConfiguration;
import io.micronaut.security.oauth2.endpoint.token.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Requires(configuration = "io.micronaut.security.token.jwt")
@Named("oidc")
public class JHipsterOpenIdUserDetailsMapper extends DefaultOpenIdUserDetailsMapper {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOpenIdUserDetailsMapper.class);
    public static final String GROUPS_CLAIM = "groups";
    public static final String ROLES_CLAIM = "roles";

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


    /**
     * @param providerName The OpenID provider name
     * @param tokenResponse The token response
     * @param openIdClaims The OpenID claims
     * @return The roles to set in the {@link UserDetails}
     */
    @Override
    protected List<String> getRoles(String providerName, OpenIdTokenResponse tokenResponse, OpenIdClaims openIdClaims) {
        String idToken = tokenResponse.getIdToken();
        try {
            return extractAuthorityFromClaims(JWTParser.parse(tokenResponse.getAccessToken()).getJWTClaimsSet());
        } catch (ParseException e) {
            LOG.error("JWT Parse exception processing id token: {}", idToken);
        }

        return Collections.emptyList();
    }

    public static List<String> extractAuthorityFromClaims(JWTClaimsSet claimsSet) {
        Object claimObject = claimsSet.getClaim(GROUPS_CLAIM);
        if (claimObject == null) {
            claimObject = claimsSet.getClaim(ROLES_CLAIM);
        }

        return mapRolesToGrantedAuthorities(getRolesFromClaims(claimObject));
    }
    @SuppressWarnings("unchecked")
    private static Collection<String> getRolesFromClaims(Object claims) {
        return (Collection<String>) claims;
    }

    private static List<String> mapRolesToGrantedAuthorities(Collection<String> roles) {
        return roles.stream()
            .filter(role -> role.startsWith("ROLE_"))
            .collect(Collectors.toList());
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
