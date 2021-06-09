package com.yh20studio.springbootwebservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String resource;

    @Builder
    OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
                             String picture, String resource){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.resource = resource;
    }

    public static OAuthAttributes of (String registrationId,
                                      String userNameAttributeName,
                                      Map<String, Object> attributes){
        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .resource(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
