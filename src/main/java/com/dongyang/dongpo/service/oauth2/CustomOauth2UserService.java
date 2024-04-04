package com.dongyang.dongpo.service.oauth2;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Role;
import com.dongyang.dongpo.dto.OAuth2Attribute;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oauth2Attribute = OAuth2Attribute.createAttribute(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> memberAttribute = oauth2Attribute.converToMap();

        String email = (String) memberAttribute.get("email");
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()){
            memberAttribute.put("exists", true);

            return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(findMember.get().getRole().name())),
                    memberAttribute, "email");

        }else {
            memberAttribute.put("exists", false);
            return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_MEMBER.name())),
                    memberAttribute, "email");
        }
    }
}
