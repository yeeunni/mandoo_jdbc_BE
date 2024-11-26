package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.ReportConverter;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.ReportsService.ReportService;
import mandooparty.mandoo.web.dto.ReportDTO;
import org.springframework.web.bind.annotation.*;

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
            return ApiResponse.onSuccess(ReportConverter.commentReportResponseDto(
                    reportService.createCommentReport(commentId, request.getMemberId())));
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),ReportConverter.commentReportResponseDto(reportService.createCommentReport(commentId, request.getMemberId())));
        }
    }
}
