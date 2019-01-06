package com.cmlatitude.configuration.system;

import com.cmlatitude.annotation.EnableOAuth2SystemClient;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class OAuth2TokenProviderImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes attributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes
                                (EnableOAuth2SystemClient.class.getName(), false));

        String clientDetailsConfigurationClass = ClientCredentialsConfiguration.class.getCanonicalName();
        if (SystemOAuth2Provider.PASSWORD == attributes.get("provider")) {
            clientDetailsConfigurationClass = ResourceOwnerPasswordConfiguration.class.getCanonicalName();
        }

        return new String[]{clientDetailsConfigurationClass};
    }
}
