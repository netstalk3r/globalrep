package com.cmlatitude.configuration.system;

import com.cmlatitude.annotation.EnableOAuth2SystemClient;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;

public class OAuth2TokenProviderImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes attributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes
                                (EnableOAuth2SystemClient.class.getName(), false));

        Class<?> provider = attributes.getClass("provider");
        String clientDetailsConfigurationClass = ClientCredentialsConfiguration.class.getCanonicalName();

        if (AuthorizationCodeAccessTokenProvider.class.getCanonicalName().equals(provider.getCanonicalName())) {
            clientDetailsConfigurationClass = AuthorizationCodeConfiguration.class.getCanonicalName();
        } else if (ResourceOwnerPasswordAccessTokenProvider.class.getCanonicalName().equals(provider.getCanonicalName())) {
            clientDetailsConfigurationClass = ResourceOwnerPasswordConfiguration.class.getCanonicalName();
        }

        return new String[]{clientDetailsConfigurationClass};
    }
}
