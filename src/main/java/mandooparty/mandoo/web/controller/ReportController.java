package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.ReportConverter;
import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.ReportsService.ReportService;
import mandooparty.mandoo.web.dto.ReportDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    @PostMapping("/sellPost/{sellPostId}")
    public ApiResponse<ReportDTO.SellPostReportResponseDto> createPostReport(
            @PathVariable("sellPostId") Long sellPostId,
            @RequestBody ReportDTO.MemberIdRequestDto request)
    {
           try{
               return ApiResponse.onSuccess(ReportConverter.sellPostReportResponseDto(
                       reportService.createSellPostReport(sellPostId, request.getMemberId())));
           }catch (GlobalException e){
               return ApiResponse.onFailure(e.getErrorCode(),ReportConverter.sellPostReportResponseDto(reportService.createSellPostReport(sellPostId, request.getMemberId())));
           }
    }

    @PostMapping("/comment/{commentId}")
    public ApiResponse<ReportDTO.CommentReportResponseDto> createCommentReport(
            @PathVariable("commentId") Long commentId,
            @RequestBody ReportDTO.MemberIdRequestDto request)
    {
        try{
            Map<String, Object> resultMap = reportService.createCommentReport(commentId, request.getMemberId());

            // Map에서 필요한 값 추출
            CommentReport commentReport = (CommentReport) resultMap.get("commentReport");
            Comment comment = (Comment) resultMap.get("comment");

            return ApiResponse.onSuccess(ReportConverter.commentReportResponseDto(
                    commentReport,comment));
        }catch (GlobalException e){
            Map<String, Object> resultMap = reportService.createCommentReport(commentId, request.getMemberId());

            // Map에서 필요한 값 추출
            CommentReport commentReport = (CommentReport) resultMap.get("commentReport");
            Comment comment = (Comment) resultMap.get("comment");
            return ApiResponse.onFailure(e.getErrorCode(),ReportConverter.commentReportResponseDto(commentReport,comment));
        }
    }
}
