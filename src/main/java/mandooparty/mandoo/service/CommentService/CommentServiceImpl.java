package mandooparty.mandoo.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
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
    public CommentDTO.CommentResponseDto createComment(CommentDTO.CommentResponseDto request) {
        // 1. Member 검증 및 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

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
        Comment comment = commentConverter.commentCreateDto(
                CommentDTO.CommentCreateDto.builder()
                        .content(request.getContent())
                        .commentStatus(request.getCommentStatus())
                        .memberId(request.getMemberId())
                        .sellPostId(request.getSellPostId())
                        .parentCommentId(request.getParentCommentId())
                        .build(),
                member,
                sellPost,
                parentComment
        );

        // 5. Comment 저장
        Comment savedComment = commentRepository.save(comment);

        // 6. 저장된 Comment를 DTO로 변환하여 반환
        return commentConverter.commentResponseDto(savedComment);
    }



}
