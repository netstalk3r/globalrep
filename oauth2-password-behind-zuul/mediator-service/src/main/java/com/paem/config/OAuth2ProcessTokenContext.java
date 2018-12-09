package com.paem.config;

public class OAuth2ProcessTokenContext {

    private final String oAuth2Token;

    private final String oAuth2TokeType;

    public OAuth2ProcessTokenContext(String oAuth2Token, String oAuth2TokeType) {
        this.oAuth2Token = oAuth2Token;
        this.oAuth2TokeType = oAuth2TokeType;
    }

    public String getOAuth2AccessToken() {
        return oAuth2Token;
    }

    public String getoAuth2TokeType() {
        return oAuth2TokeType;
    }
}
