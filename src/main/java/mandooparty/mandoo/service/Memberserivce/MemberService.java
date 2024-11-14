package mandooparty.mandoo.service.Memberserivce;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.web.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    public Member signUpMember(String email, String password, String status, String nickname);

    public MemberDTO.MemberLoginResponseDto loginMember(MemberDTO.MemberLoginDto request);
    MemberDTO.MemberLoginResponseDto logoutMember(MemberDTO.MemberLogoutDto request);
}
