package mandooparty.mandoo.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReport {

    private Long id;  // 신고 ID
    private Integer commentReportCount;  // 신고 횟수

    private Long memberId;  // 작성자 (Member 객체)
    private Long commentId;  // 댓글 (Comment 객체)

}