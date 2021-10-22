package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackListRepository;
import com.yh20studio.springbootwebservice.domain.exception.RestException;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.labelColors.LabelColorsRepository;
import com.yh20studio.springbootwebservice.domain.labels.LabelsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshTokenRepository;
import com.yh20studio.springbootwebservice.dto.httpResponse.MessageResponse;
import com.yh20studio.springbootwebservice.dto.labelColors.LabelColorsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.labels.LabelsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class MemberService {
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private RefreshTokenRepository refreshTokenRepository;
    private AccessTokenBlackListRepository accessTokenBlackListRepository;
    private LabelColorsRepository labelColorsRepository;
    private LabelsRepository labelsRepository;
    private SecurityUtil securityUtil;


    // 새롭게 가입하는 과정, 가입하는 과정에서 Labels를 생성할 수 있도록 했다.
    @Transactional
    public Long signup(MemberSaveRequestDto memberSaveRequestDto) {

        // 해당 Email로 이미 가입한 유저가 있다면 HttpStatus.CONFLICT를 Throw 한다.
        if(memberRepository.existsByEmail(memberSaveRequestDto.getEmail())){
            //409 Error
            throw new RestException(HttpStatus.CONFLICT, "이미 가입되어 있는 유저입니다.");
        }

        Member member = memberRepository.save(memberSaveRequestDto.toMember(passwordEncoder));

        // 유저가 가입을 했다면 DB에 저장되어 있는 LabelColors를 통하여 Labels를 생성한다.
        List<LabelColorsMainResponseDto> labelColorsMainResponseDtos = labelColorsRepository.findAllWithoutHoliday().map(LabelColorsMainResponseDto::new).collect(Collectors.toList());;
        for(LabelColorsMainResponseDto labelColorsMainResponseDto : labelColorsMainResponseDtos){

            // 해당 Id로 LabelColors가 존재하지 않는 다면 HttpStatus.NOT_FOUND를 throw 한다.
            LabelColors labelColors =  labelColorsRepository.findById(labelColorsMainResponseDto.getId())
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Colors를 찾지 못했습니다."));

            LabelsSaveRequestDto labelsSaveRequestDto = LabelsSaveRequestDto.builder()
                    .title(labelColorsMainResponseDto.getTitle())
                    .sequence(labelColorsMainResponseDto.getId().intValue()-1)
                    .labelColors(labelColors)
                    .member(member)
                    .build();

            labelsRepository.save(labelsSaveRequestDto.toEntity());
        }

        return member.getId();
    }

    // 토큰을 통하여 로그인 하는 과정
    @Transactional
    public TokenResponseDto login(MemberSaveRequestDto memberSaveRequestDto) {
        // 이메일과 password를 통해서 UsernamePasswordAuthenticationToken을 생성함.
        UsernamePasswordAuthenticationToken authenticationToken = memberSaveRequestDto.toAuthentication();

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성(Access, Refresh Token)
        TokenResponseDto tokenResponseDto = jwtUtil.generateTokenDto(authentication);

        // 발행된 RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .expires(tokenResponseDto.getRefreshTokenExpiresIn())
                .build();

        refreshTokenRepository.save(refreshToken);

        // (Access, Refresh Token) 토큰 리턴
        return tokenResponseDto;
    }

    // 로그인 된 사용자가 TokenRequestDto을 통해서 로그아웃 하는 과정
    @Transactional
    public MessageResponse logout(TokenRequestDto tokenRequestDto) {

        // Refresh 토큰이 유요하지 않을 경우 HttpStatus.UNAUTHORIZED Throw
        if (!jwtUtil.validateToken(tokenRequestDto.getRefreshToken())) {
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "Refresh Token 이 유효하지 않습니다.");
        }

        // Access 토큰으로 Authentication 생성
        Authentication authentication = jwtUtil.getAuthentication(tokenRequestDto.getAccessToken());

        // 현재 DB에 해당 유저의 Refresh 토큰이 존재하는지 살펴보고, 만약 없다면 이미 로그아웃된 사용자로 간주하고 HttpStatus.UNAUTHORIZED Throw
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                //401 Error
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "로그아웃 된 사용자입니다."));

        // 현재 DB에 Refresh 토큰이 존재하는 토큰과, 주어진 토큰을 비교한다. 만약 다르다면 잘못된 접근으로 간주하며, HttpStatus.UNAUTHORIZED Throw
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        // AccessToken 블랙리스트 추가
        // 로그아웃 과정을 거치면서 주어진 Refresh 토큰으로 재발급은 불가능하지만, 이미 발행되었던 Access 토큰으로 로그인을 막음
        AccessTokenRequestDto accessTokenRequestDto = AccessTokenRequestDto.builder()
                .key(refreshToken.getKey())
                .value(tokenRequestDto.getAccessToken())
                .expires(tokenRequestDto.getAccessTokenExpiresIn())
                .build();

        accessTokenBlackListRepository.save(accessTokenRequestDto.toEntity());

        // RefreshToken 삭제
        refreshTokenRepository.deleteByKey((refreshToken.getKey()));

        return new MessageResponse("Logout");
    }


    // TokenRequestDto을 통하여 토큰을 재발급
    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // Refresh 토큰 검증
        if (!jwtUtil.validateToken(tokenRequestDto.getRefreshToken())) {
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "Refresh Token 이 유효하지 않습니다");
        }

        // Access 토큰에서 Member Id 가져오기
        Authentication authentication = jwtUtil.getAuthentication(tokenRequestDto.getAccessToken());

        // DB에서 Member Id를 기반으로 Refresh Token 값 가져옴
        // 해당 토큰이 없다면 HttpStatus.UNAUTHORIZED Throw
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                //401 Error
                .orElseThrow(() -> new RestException(HttpStatus.UNAUTHORIZED, "로그아웃 된 사용자입니다."));

        // DB에 저장된 Refresh 토큰과 주어진 Refresh 토큰이 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 발급
        TokenResponseDto tokenResponseDto = jwtUtil.generateTokenDto(authentication);

        // DB 정보 업데이트
        RefreshToken newRefreshToken = refreshToken
                .updateValue(tokenResponseDto.getRefreshToken())
                .updateExpires(tokenResponseDto.getRefreshTokenExpiresIn());

        refreshTokenRepository.save(newRefreshToken);

        return tokenResponseDto;
    }

    // 현재 SecurityContext에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberMainResponseDto getMyInfo() {
        return memberRepository.findById(securityUtil.getCurrentMemberId())
                .map(MemberMainResponseDto::new)
                // 404 error
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "로그인 유저 정보가 없습니다."));
    }
}
