package io.github.jhipster.sample.security;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata;
import io.micronaut.security.oauth2.configuration.endpoints.EndSessionConfiguration;
import io.micronaut.security.oauth2.endpoint.endsession.request.EndSessionEndpoint;

import javax.inject.Named;
import javax.inject.Singleton;
import java.net.URI;

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
public class KeycloakEndSessionEndpoint implements EndSessionEndpoint {

    public static final String PARAM_REDIRECT_URI = "redirect_uri";
    private final OpenIdProviderMetadata openIdProviderMetadata;
    private final EndSessionConfiguration endSessionConfiguration;
    private final HttpHostResolver httpHostResolver;

    public KeycloakEndSessionEndpoint(@Named("oidc") OpenIdProviderMetadata openIdProviderMetadata,
                                      EndSessionConfiguration endSessionConfiguration,
                                      HttpHostResolver httpHostResolver) {
        this.openIdProviderMetadata = openIdProviderMetadata;
        this.endSessionConfiguration = endSessionConfiguration;
        this.httpHostResolver = httpHostResolver;
    }

    @Nullable
    @Override
    public String getUrl(HttpRequest originating, Authentication authentication) {
        if (openIdProviderMetadata.getEndSessionEndpoint() == null) {
            return null;
        }
        return UriBuilder.of(URI.create(openIdProviderMetadata.getEndSessionEndpoint()))
            .queryParam(PARAM_REDIRECT_URI, httpHostResolver.resolve(originating) + endSessionConfiguration.getRedirectUri())
            .build()
            .toString();
    }
}


