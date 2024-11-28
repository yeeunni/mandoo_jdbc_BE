package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.web.dto.CommentDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    // DTO -> Entity: ëê¸ ìì±
    public Comment commentCreateDto(CommentDTO.CommentCreateDto dto, Member member, SellPost sellPost, Comment parentComment) {
        return Comment.builder()
                .content(dto.getContent())
                .commentStatus(dto.getCommentStatus())
                .member(member)
                .sellPost(sellPost)
                .parentComment(parentComment) // ë¶ëª¨ ëê¸ì´ ìì ê²½ì° null ì²ë¦¬
                .build();
    }

    // Entity -> DTO: ëê¸ ìëµ
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