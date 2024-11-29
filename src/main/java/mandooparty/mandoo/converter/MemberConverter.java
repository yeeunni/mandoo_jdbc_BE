package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.enums.MemberStatus;
import mandooparty.mandoo.web.dto.MemberDTO;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {
    public static MemberDTO.MemberSignUpDto memberSignUpDto(String email, String name,String nickname,String status, String password){
        return MemberDTO.MemberSignUpDto.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .status(status)
                .build();
    }
    public static MemberDTO.MemberSignUpResponseDto memberSignUpResponseDto(Member member){
        return MemberDTO.MemberSignUpResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .status(member.getStatus())
                .build();
    }
    public static MemberDTO.MemberLoginDto memberLoginDto(String email,String password){
        return MemberDTO.MemberLoginDto.builder()
                .email(email)
                .password(password)
                .build();
    }
    public static MemberDTO.MemberLoginResponseDto memberLoginResponseDto(Member member){
        return MemberDTO.MemberLoginResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .status(String.valueOf(member.getStatus()))
                .isLogin(member.getIs_login())
                .build();
    }
}
