package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.MemberConverter;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.Memberserivce.MemberService;
import mandooparty.mandoo.web.dto.MemberDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/signup")
    public ApiResponse<MemberDTO.MemberSignUpResponseDto> signUpMember(@RequestBody MemberDTO.MemberSignUpDto request){
        String email = request.getEmail();
        String password = request.getPassword();
        String status = request.getStatus();
        String nickname = request.getNickname();
        try{
            return ApiResponse.onSuccess(MemberConverter.memberSignUpResponseDto(memberService.signUpMember(email, password, status, nickname)));
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(), MemberConverter.memberSignUpResponseDto(memberService.signUpMember(email, password, status, nickname)));
        }
    }
    @PostMapping("/login")
    public ApiResponse<MemberDTO.MemberLoginResponseDto> login(@RequestBody MemberDTO.MemberLoginDto request) {
        try{
            return ApiResponse.onSuccess(memberService.loginMember(request));
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),memberService.loginMember(request));
        }
    }
    @PostMapping("/logout")
    public ApiResponse<MemberDTO.MemberLoginResponseDto> logout(@RequestBody MemberDTO.MemberLogoutDto request) {
        try{
            return ApiResponse.onSuccess(memberService.logoutMember(request));
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),memberService.logoutMember(request));
        }
    }
}
