package mandooparty.mandoo.domain;
import mandooparty.mandoo.domain.enums.CommentStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;  // 댓글 ID
    private String content;  // 댓글 내용
    private CommentStatus commentStatus;  // 댓글 상태

    private Long memberId;  // 작성자 (Member 객체)
    private Long sellPostId;  // 게시물 (SellPost 객체)

    private Long parentCommentId;  // 부모 댓글 (답글의 경우)

}