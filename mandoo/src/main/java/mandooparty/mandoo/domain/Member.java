package mandooparty.mandoo.domain;

import lombok.*;
import mandooparty.mandoo.domain.enums.MemberStatus;

import java.time.LocalDateTime;


@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private MemberStatus status; // Enum 대신 문자열로 처리
    private Boolean isLogin;
    private Integer writeSellPostCount;
    private Integer likeSellPostCount;
    private LocalDateTime loginTime;
    private Integer completedSellPostCount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
