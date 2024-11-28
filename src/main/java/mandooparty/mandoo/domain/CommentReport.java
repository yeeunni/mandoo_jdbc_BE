package mandooparty.mandoo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReport {

    private Long id;  // 신고 ID
    private Integer comment_report_count;  // 신고 횟수

    private Long member_id;  // 작성자 (Member 객체)
    private Long comment_id;  // 댓글 (Comment 객체)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}