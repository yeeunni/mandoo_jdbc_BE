package mandooparty.mandoo.service.ReportsService;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.domain.*;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
                postReport.setPostReportCount(postReport.getPostReportCount()+1);
                return postReport;
            }else{
                PostReport postReport=PostReport.builder()
                        .sellPost(sellPost)
                        .member(member)
                        .postReportCount(1)
                        .build();
                postReportRepository.save(postReport);
                return postReport;
            }
        }

    }

    public CommentReport createCommentReport(Long commentId, Long memberId) {
        Optional<Member> findMember=memberRepository.findById(memberId);
        Optional<Comment> findComment=commentRepository.findById(commentId);
        System.out.println("commentId: "+commentId);
        if(findMember.isEmpty()){
            throw new GlobalException(GlobalErrorCode.INVALID_MEMBER_STATUS);
        }else if(findComment.isEmpty()){
            throw new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND);
        }else{
            Member member=findMember.get();
            Comment comment=findComment.get();
            Optional<CommentReport> findCommentReport=commentReportRepository.findByCommentAndMember(comment,member);
            if(findCommentReport.isPresent()){
                CommentReport commentReport=findCommentReport.get();
                commentReport.setCommentReportCount(commentReport.getCommentReportCount()+1);
                return commentReport;
            }else{
                CommentReport commentReport=CommentReport.builder()
                                .comment(comment)
                                .member(member)
                                .commentReportCount(1)
                                .build();
                commentReportRepository.save(commentReport);
                return commentReport;
            }
        }
    }
}
