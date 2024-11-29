package mandooparty.mandoo.domain;
import mandooparty.mandoo.domain.enums.CommentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;  // 댓글 ID
    private String content;  // 댓글 내용
    private Integer comment_status;  // 댓글 상태 0:공개 1:비밀 2:후기

    private Long member_id;  // 작성자 (Member 객체)
    private Long sell_post_id;  // 게시물 (SellPost 객체)

    private Long parent_comment_id;  // 부모 댓글 (답글의 경우)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}