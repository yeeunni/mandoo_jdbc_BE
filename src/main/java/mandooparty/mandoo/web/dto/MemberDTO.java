package mandooparty.mandoo.web.dto;
import lombok.*;


import java.util.List;
import java.util.Set;
public class MemberDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSignUpDto{
        private String email;
        private String password;
        private String nickname;
        private String status;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSignUpResponseDto{
        private Long memberId;
        private String email;
        private String password;
        private String nickname;
        private Integer status;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginDto{
        private String email;
        private String password;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberChangePasswordDto{
        private String email;
        private String password;
        private String newPassword;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLogoutDto{
        private String email;
        private Long memberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginResponseDto{
        private Long memberId;
        private String email;
        private String nickname;
        private String status;
        private Integer isLogin;
    }


}
