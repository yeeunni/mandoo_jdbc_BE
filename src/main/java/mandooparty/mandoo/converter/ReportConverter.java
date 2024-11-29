package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.PostReport;
import mandooparty.mandoo.web.dto.ReportDTO;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {

    public static ReportDTO.SellPostReportResponseDto sellPostReportResponseDto(PostReport postReport){
        return ReportDTO.SellPostReportResponseDto.builder()
                .sellPostReportId(postReport.getId())
                .sellPostId(postReport.getSell_post_id())
                .memberId(postReport.getMember_id())
                .postReportCount(postReport.getPost_report_count())
                .build();
    }

    public static ReportDTO.CommentReportResponseDto commentReportResponseDto(CommentReport commentReport, Comment comment){
        return ReportDTO.CommentReportResponseDto.builder()
                .commentReportId(commentReport.getId())
                .commentId(comment.getId())
                .memberId(commentReport.getMember_id())
                .commentReportCount(commentReport.getComment_report_count())
                .build();
    }
}
