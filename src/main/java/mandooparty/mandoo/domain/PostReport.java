package mandooparty.mandoo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReport {

    private Long id;
    private Integer post_report_count;
    private Long member_id;      // Member의 ID
    private Long sell_post_id;    // SellPost의 ID
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}