package com.jiraynor.oauthjwt.service;

import com.jiraynor.oauthjwt.dto.CustomOAuth2User;
import com.jiraynor.oauthjwt.dto.KakaoResponse;
import com.jiraynor.oauthjwt.dto.OAuth2Response;
import com.jiraynor.oauthjwt.dto.UserDTO;
import com.jiraynor.oauthjwt.entity.UserEntity;
import com.jiraynor.oauthjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("OAuth2UserRequest: " + userRequest);
        System.out.println("OAuth2User: " + oAuth2User.getAttributes());
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        System.out.println("OAuth2Response: " + oAuth2Response);
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);
        System.out.println("username = " + username);
        System.out.println("Existing User: " + existData);

        if (existData == null) {
            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDTO);
        } else {
            UserDTO userDTO = UserDTO.builder()
                    .username(existData.getUsername())
                    .name(existData.getName())
                    .role(existData.getRole())
                    .build();
            return new CustomOAuth2User(userDTO);
        }
    }
}
