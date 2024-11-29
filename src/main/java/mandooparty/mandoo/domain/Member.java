package mandooparty.mandoo.domain;

import lombok.*;
import mandooparty.mandoo.domain.enums.MemberStatus;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private Integer status; // status:0 -> 사용자, status:1 -> 관리자
    private Boolean isLogin;
    private Integer writeSellPostCount;
    private Integer likeSellPostCount;
    private LocalDateTime loginTime;
    private Integer completedSellPostCount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
