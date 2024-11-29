package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.web.dto.CommentDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    // DTO -> Entity: 댓글 생성
    public Comment commentCreateDto(CommentDTO.CommentCreateDto dto) {
        return Comment.builder()
                .content(dto.getContent())
                .comment_status(dto.getCommentStatus())
                .member_id(dto.getMemberId())
                .sell_post_id(dto.getSellPostId())
                .parent_comment_id(dto.getParentCommentId()) // 부모 댓글이 없을 경우 null 처리
                .build();
    }

    // Entity -> DTO: 댓글 응답
    public CommentDTO.CommentResponseDto commentResponseDto(Comment comment,Member member) {
        return CommentDTO.CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .commentStatus(comment.getComment_status())
                .memberId(comment.getMember_id())
                .sellPostId(comment.getSell_post_id())
                .parentCommentId(comment.getParent_comment_id() != null ? comment.getParent_comment_id() : null)
                .createdAt(comment.getCreated_at())
                .modifiedAt(comment.getUpdated_at())
                .build();
    }
}