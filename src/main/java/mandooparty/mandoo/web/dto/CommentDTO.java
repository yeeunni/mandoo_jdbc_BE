package mandooparty.mandoo.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class CommentDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentCreateDto {
        private String content;          // 댓글 내용
        private Integer commentStatus; // 댓글 공개 여부
        private Long memberId;          // 작성자 ID
        private Long sellPostId;        // 게시글 ID
        private Long parentCommentId;   // 부모 댓글 ID (답글의 경우)
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponseDto {
        private Long commentId;         // 댓글 ID
        private String content;         // 댓글 내용
        private Integer commentStatus; // 댓글 공개 여부
        private Long memberId;          // 작성자 ID
        private String memberNickname;  // 작성자 닉네임
        private Long sellPostId;        // 게시글 ID
        private Long parentCommentId;   // 부모 댓글 ID (답글일 경우)
        private LocalDateTime createdAt;  // 작성일
        private LocalDateTime modifiedAt; // 수정일
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentUpdateDto {
        private Long commentId;         // 댓글 ID
        private String content;         // 수정할 댓글 내용
        private Integer commentStatus; // 수정할 댓글 공개 여부
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDeleteDto {
        private Long commentId;         // 삭제할 댓글 ID
    }
}
