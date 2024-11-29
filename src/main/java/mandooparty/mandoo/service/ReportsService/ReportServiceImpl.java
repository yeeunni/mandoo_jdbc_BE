package mandooparty.mandoo.service.ReportsService;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.domain.*;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final SellPostRepository sellPostRepository;
    private final PostReportRepository postReportRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    public PostReport createSellPostReport(Long sellPostId,Long memberId) {

        Optional<SellPost> findSellPost=sellPostRepository.findById(sellPostId);
        Optional<Member> findMember=memberRepository.findById(memberId);
        if(findSellPost.isEmpty()){
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }else if(findMember.isEmpty()){
            throw new GlobalException(GlobalErrorCode.INVALID_MEMBER_STATUS);
        }else{
            SellPost sellPost=findSellPost.get();
            Member member=findMember.get();
            Optional<PostReport> findPostReport=postReportRepository.findBySellPostAndMember(sellPost,member);
            if(findPostReport.isPresent()){
                PostReport postReport=findPostReport.get();
                postReport.setPost_report_count(postReport.getPost_report_count()+1);
                return postReport;
            }else{
//                PostReport postReport=new PostReport(
//                        0,
//                        1,
//                        member.getId(),
//                        sellPost.getSell_post_id(),
//                        LocalDateTime.now(),
//                        LocalDateTime.now()
//                );
                PostReport postReport=PostReport.builder()
                        .sell_post_id(sellPost.getSell_post_id())
                        .member_id(member.getId())
                        .post_report_count(1)
                        .build();
                postReportRepository.insertPostReport(postReport);
                return postReport;
            }
        }

    }

    public Map<String, Object> createCommentReport(Long commentId, Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Optional<Comment> findComment = commentRepository.findById(commentId);

        if (findMember.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.INVALID_MEMBER_STATUS);
        } else if (findComment.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND);
        } else {
            Member member = findMember.get();
            Comment comment = findComment.get();
            Optional<CommentReport> findCommentReport = commentReportRepository.findByCommentAndMember(comment, member);

            CommentReport commentReport;

            if (findCommentReport.isPresent()) {
                commentReport = findCommentReport.get();
                commentReport.setComment_report_count(commentReport.getComment_report_count() + 1);
            } else {
                commentReport = CommentReport.builder()
                        .comment_id(comment.getId())
                        .member_id(member.getId())
                        .comment_report_count(1)
                        .build();
                commentReportRepository.insertCommentReport(commentReport);
            }

            // 결과를 Map으로 묶어서 반환
            Map<String, Object> result = new HashMap<>();
            result.put("commentReport", commentReport);
            result.put("comment", comment);

            return result;
        }
    }

}
