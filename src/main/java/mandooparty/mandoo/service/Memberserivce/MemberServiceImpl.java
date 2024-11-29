package mandooparty.mandoo.service.Memberserivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.converter.MemberConverter;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.MemberRepository;
import mandooparty.mandoo.web.dto.MemberDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static mandooparty.mandoo.converter.MemberConverter.memberLoginResponseDto;
import static mandooparty.mandoo.converter.MemberConverter.memberSignUpResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member signUpMember(String email, String password, String status, String nickname) {

        Optional<Member> findMemberByEmail = memberRepository.findByEmail(email); //메일로 사용자 찾기
        Optional<Member> findMemberByNickname = memberRepository.findByNickname(nickname); //닉네임으로 사용자 찾기

        if (findMemberByEmail.isPresent()) { //이메일이 이미 있을 시
            throw new GlobalException(GlobalErrorCode.DUPLICATE_EMAIL);
        }
        if (findMemberByNickname.isPresent()) { //닉네임이 이미 있을 시
            throw new GlobalException(GlobalErrorCode.DUPLICATE_NICKNAME);
        }

        // String status 값을 int형으로 변환
       int memberStatus;
        if ("user".equalsIgnoreCase(status)) {
            memberStatus = 0;
        } else if ("manager".equalsIgnoreCase(status)) {
            memberStatus = 1;
        } else {
            throw new GlobalException(GlobalErrorCode.INVALID_MEMBER_STATUS);
        }
        Member member = Member.builder()
                .email(email)
                .password(password)
                .status(memberStatus) // Enum 값을 문자열로 저장
                .nickname(nickname)
                .is_login(false) // 기본값 설정
                .write_sell_post_count(0) // 기본값 설정
                .like_sell_post_count(0) // 기본값 설정
                .completed_sell_post_count(0) // 기본값 설정
                .login_time(null) // 기본값 설정
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        Member saveData=memberRepository.insertMember(member);
        return saveData;
    }

    @Override
    @Transactional
    public MemberDTO.MemberLoginResponseDto loginMember(MemberDTO.MemberLoginDto request) {
        String email = request.getEmail(); //요청의 이메일
        String password = request.getPassword(); //요청의 비밀번호

        Optional<Member> findMemberByEmail = memberRepository.findByEmail(email); //이메일로 가입된 사용자를 찾음
        if (findMemberByEmail.isPresent()) { //해당 이메일을 가진 사용자가 있다면(가입이 되어있다면)
            Member member = findMemberByEmail.get(); //사용자 객체를 가져옴

            if (password.equals(member.getPassword())) { //만약 요청의 패스워드마저 같으면
                boolean updated = memberRepository.updateLoginStatusByEmail(email, 1);
                LocalDateTime currentLoginTime = LocalDateTime.now(); // 현재 시간
                boolean updatedLoginTime = memberRepository.updateLoginTimeByEmail(email, currentLoginTime);
                member.setLogin_time(LocalDateTime.now());
                return memberLoginResponseDto(member); //사용자의 정보를 return해준다.
            } else {
                throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH); //비밀번호가 일치하지않는다면
            }
        } else {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND); //해당 이메일로 가입되어 있지 않습니다.
        }
    }

    @Override
    @Transactional
    public MemberDTO.MemberLoginResponseDto logoutMember(MemberDTO.MemberLogoutDto request) {
        String email = request.getEmail(); // 요청의 이메일

        Optional<Member> findMemberByEmail = memberRepository.findByEmail(email);
        if (findMemberByEmail.isPresent()) {
            Member member = findMemberByEmail.get();

            // 로그인 상태인지 확인
            if (member.getIs_login()) { //isLogin이 true면 -> 사용자가 로그인중이라는 뜻
                member.setIs_login(false);  // isLogin 상태를 false로 설정하여 로그아웃 처리
                return memberLoginResponseDto(member); // 로그아웃 완료 후 정보 반환
            } else {
                throw new GlobalException(GlobalErrorCode.ALREADY_LOGGED_OUT); // 이미 로그아웃 상태일 때 예외 발생
            }
        } else {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND); // 이메일로 회원을 찾지 못한 경우 예외 발생
        }
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        Optional <Member> findMember = memberRepository.findById(memberId);

        if (findMember.isPresent()) { //사용자가 있고
            Member member = findMember.get();
            if (member.getIs_login()){ //로그인 상태이고
                memberRepository.deleteById(memberId); // JPA 기본 메서드 사용
            }else{
                throw new GlobalException(GlobalErrorCode.ALREADY_LOGGED_OUT); // 이미 로그아웃 상태일 때 예외 발생
            }

        } else {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND); // 멤버를 찾지 못한 경우 예외 처리
        }
    }

}
