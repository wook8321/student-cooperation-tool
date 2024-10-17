package com.stool.studentcooperationtools.security.oauth2.dto;


import com.stool.studentcooperationtools.domain.member.Member;
import com.stool.studentcooperationtools.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String,Object> attributes;
    private String attributesNameKey;
    private String userName;
    private String name;
    private String email;
    private String picture;

    @Builder
    private OAuthAttributes(
            final Map<String, Object> attributes,
            final String attributesNameKey,
            final String name,
            final String userName,
            final String email,
            final String picture
    ) {
        this.attributes = attributes;
        this.attributesNameKey = attributesNameKey;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(
            final String registrationId,
            final String attributeName,
            final Map<String, Object> attributes
    ) {
        return ofGoogle(attributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(final String attributeName, final Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .attributesNameKey(attributeName)
                .userName((String) attributes.get("name"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickName(userName)
                .email(email)
                .profile(picture)
                .role(Role.USER)
                .build();
    }
}
