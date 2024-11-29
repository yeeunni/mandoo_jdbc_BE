package mandooparty.mandoo.domain;

import lombok.*;

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
    private Boolean is_login;
    private Integer write_sell_post_count;
    private Integer like_sell_post_count;
    private LocalDateTime login_time;
    private Integer completed_sell_post_count;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
