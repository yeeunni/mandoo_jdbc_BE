package mandooparty.mandoo.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.CommentRepository;
import mandooparty.mandoo.repository.MemberRepository;
import mandooparty.mandoo.repository.SellPostRepository;
import mandooparty.mandoo.converter.CommentConverter;
import mandooparty.mandoo.web.dto.CommentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final SellPostRepository sellPostRepository;
    private final CommentConverter commentConverter;

    @Override
    @Transactional
    public CommentDTO.CommentResponseDto createComment(CommentDTO.CommentCreateDto request) {
        // 1. Member 검증 및 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND));

        // 2. SellPost 검증 및 조회
        SellPost sellPost = sellPostRepository.findById(request.getSellPostId())
                .orElseThrow(() -> new IllegalArgumentException("SellPost not found"));

        // 3. 부모 댓글 검증 및 조회 (Optional)
        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        // 4. Comment 엔티티 생성
        Comment comment = commentConverter.commentCreateDto(request);

        // Null 값 기본값 설정 (여기서 명시적으로 설정)
        if (comment.getComment_status() == null) {
            log.info("Inserting Comment with status: {}", comment.getComment_status());
            log.info("Inserting Comment with member: {}", comment.getMember_id());
            log.info("Inserting Comment with sellpost: {}", comment.getSell_post_id());
            log.info("Inserting Comment with content: {}", comment.getContent());
        }

        // 5. Comment 저장
         commentRepository.insertComment(comment);

        // 6. 저장된 Comment를 DTO로 변환하여 반환
        return commentConverter.commentResponseDto(comment,member);
    }



}
