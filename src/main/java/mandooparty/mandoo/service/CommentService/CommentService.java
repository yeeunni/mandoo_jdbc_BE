package mandooparty.mandoo.service.CommentService;

import mandooparty.mandoo.web.dto.CommentDTO;

public interface CommentService {

    CommentDTO.CommentResponseDto createComment(CommentDTO.CommentCreateDto request);
}
