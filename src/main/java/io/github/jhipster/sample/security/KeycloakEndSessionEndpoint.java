package io.github.jhipster.sample.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.oauth2.endpoint.endsession.request.AbstractEndSessionRequest;
import io.micronaut.security.oauth2.endpoint.endsession.response.EndSessionCallbackUrlBuilder;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdUserDetailsMapper;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides specific configuration to logout from Okta.
 *
 * @see <a href="https://developer.okta.com/docs/api/resources/oidc/#logout">Okta Logout Endpont</a>
 *
 * @author Sergio del Amo
 * @since 1.2.0
 */
@Named("oidc")
@Singleton
public class KeycloakEndSessionEndpoint extends AbstractEndSessionRequest {

    private static final String PARAM_POST_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri";
    private static final String PARAM_ID_TOKEN_HINT = "id_token_hint";

    /**
     * @deprecated use {@link #KeycloakEndSessionEndpoint(EndSessionCallbackUrlBuilder, OauthClientConfiguration, Supplier)} instead.
     * @param endSessionCallbackUrlBuilder The end session callback URL builder
     * @param clientConfiguration The client configuration
     * @param providerMetadata The provider metadata
     */
    @Deprecated
    public KeycloakEndSessionEndpoint(EndSessionCallbackUrlBuilder endSessionCallbackUrlBuilder,
                                  OauthClientConfiguration clientConfiguration,
                                  OpenIdProviderMetadata providerMetadata) {
        super(endSessionCallbackUrlBuilder, clientConfiguration, providerMetadata);
    }

    /**
     * @param endSessionCallbackUrlBuilder The end session callback URL builder
     * @param clientConfiguration The client configuration
     * @param providerMetadata The provider metadata supplier
     */
    public KeycloakEndSessionEndpoint(EndSessionCallbackUrlBuilder endSessionCallbackUrlBuilder,
                                  OauthClientConfiguration clientConfiguration,
                                  Supplier<OpenIdProviderMetadata> providerMetadata) {
        super(endSessionCallbackUrlBuilder, clientConfiguration, providerMetadata);
    }

    @Override
    protected String getUrl() {
        System.out.println("getUrl: " + providerMetadataSupplier.get().getEndSessionEndpoint());
        return providerMetadataSupplier.get().getEndSessionEndpoint();
    }

    @Override
    protected Map<String, Object> getArguments(HttpRequest originating,
                                               Authentication authentication) {
        System.out.println("getArguments: " + originating.getUri().toString());
        Map<String, Object> attributes = authentication.getAttributes();
        Map<String, Object> arguments = new HashMap<>();
        if (attributes.containsKey(OpenIdUserDetailsMapper.OPENID_TOKEN_KEY)) {
            arguments.put(PARAM_ID_TOKEN_HINT, attributes.get(OpenIdUserDetailsMapper.OPENID_TOKEN_KEY));
        }
        arguments.put(PARAM_POST_LOGOUT_REDIRECT_URI, getRedirectUri(originating));
        return arguments;
    }

}


