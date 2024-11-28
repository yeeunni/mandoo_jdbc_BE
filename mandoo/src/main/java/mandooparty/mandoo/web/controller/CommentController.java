package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.service.CommentService.CommentService;
import mandooparty.mandoo.web.dto.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellpost")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/read/{id}")
    public ResponseEntity<CommentDTO.CommentResponseDto> createComment(
            @RequestBody CommentDTO.CommentCreateDto request) {
        CommentDTO.CommentResponseDto response = commentService.createComment(
                CommentDTO.CommentResponseDto.builder()
                        .content(request.getContent())
                        .commentStatus(request.getCommentStatus())
                        .memberId(request.getMemberId())
                        .sellPostId(request.getSellPostId())
                        .parentCommentId(request.getParentCommentId())
                        .build()
        );
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/read/{id}")
//    public ResponseEntity<CommentDTO.CommentResponseDto> getComment(
//            @RequestBody CommentDTO.CommentCreateDto request,
//            @PathVariable Long sellPostId)


}
