package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.PostReport;
import mandooparty.mandoo.web.dto.ReportDTO;
import org.springframework.stereotype.Component;

@Component
public class ReportConverter {

    public static ReportDTO.SellPostReportResponseDto sellPostReportResponseDto(PostReport postReport){
        return ReportDTO.SellPostReportResponseDto.builder()
                .sellPostReportId(postReport.getId())
                .sellPostId(postReport.getSellPost().getSellPostId())
                .memberId(postReport.getMember().getId())
                .postReportCount(postReport.getPostReportCount())
                .build();
    }

    public static ReportDTO.CommentReportResponseDto commentReportResponseDto(CommentReport commentReport){
        return ReportDTO.CommentReportResponseDto.builder()
                .commentReportId(commentReport.getId())
                .commentId(commentReport.getComment().getId())
                .memberId(commentReport.getMember().getId())
                .commentReportCount(commentReport.getCommentReportCount())
                .build();
    }
}
