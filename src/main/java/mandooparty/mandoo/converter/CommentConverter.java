package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.web.dto.CommentDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    // DTO -> Entity: 댓글 생성
    public Comment commentCreateDto(CommentDTO.CommentCreateDto dto, Member member, SellPost sellPost, Comment parentComment) {
        return Comment.builder()
                .content(dto.getContent())
                .commentStatus(dto.getCommentStatus())
                .member(member)
                .sellPost(sellPost)
                .parentComment(parentComment) // 부모 댓글이 없을 경우 null 처리
                .build();
    }

    // Entity -> DTO: 댓글 응답
    public CommentDTO.CommentResponseDto commentResponseDto(Comment comment) {
        return CommentDTO.CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .commentStatus(comment.getCommentStatus())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .sellPostId(comment.getSellPost().getSellPostId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getUpdateAt())
                .build();
    }
}