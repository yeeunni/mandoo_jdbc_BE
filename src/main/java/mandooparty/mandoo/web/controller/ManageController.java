package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.ManageConverter;
import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.ManageService.ManageService;
import mandooparty.mandoo.web.dto.ManageDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManageController {
    private final ManageService manageService;
    @GetMapping("/dashboard")//관리자 페이지 dashboard
    public ApiResponse<ManageDTO.ManageDashBoardDto> ManageDashBoard(){
        List<ManageDTO.ManageDashBoardSellPostDto> sellPostCountList=manageService.getDaySellPostCount();
        List<ManageDTO.ManageDashBoardCategoryRatioDto> categoryRatio=manageService.getCategoryRatio();
        List<ManageDTO.ManageDashBoardDateViewDto> dateView=manageService.getDateView();

        ManageDTO.ManageDashBoardDto manageDashBoardDto=new ManageDTO.ManageDashBoardDto(sellPostCountList,categoryRatio,dateView);

        try{
            return ApiResponse.onSuccess(manageDashBoardDto);
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),manageDashBoardDto);
        }
    }

    @GetMapping("/member")//관리자 페이지 회원관리
    public ApiResponse<List<ManageDTO.ManageMemberDto>> ManageMember(@RequestParam(value = "order", required = false, defaultValue = "id") String order){
        List<Member> memberList=manageService.getMember(order);

        List<ManageDTO.ManageMemberDto> manageMemberDtoList=new ArrayList<>();
        for(Member member : memberList){//domain -> dto로 변경
            manageMemberDtoList.add(ManageConverter.ManageMemberDto(member));
        }
        try{
            return ApiResponse.onSuccess(manageMemberDtoList);
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(), manageMemberDtoList);
        }

    }

    @GetMapping("/report/comment")
    public ApiResponse<List<ManageDTO.CommentReportDto>> ManageCommentReport(@RequestParam(value = "order", required = false, defaultValue = "id") String order){

        List<ManageDTO.CommentReportDto> commentReportDtoList=manageService.getCommentReport(order);
        try{
            return ApiResponse.onSuccess(commentReportDtoList);
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),commentReportDtoList);
        }
    }

    @GetMapping("/report/sellPost")
    public ApiResponse<List<ManageDTO.PostReportDto>> ManagePostReport(@RequestParam(value = "order", required = false, defaultValue = "id") String order){

        List<ManageDTO.PostReportDto> postReportDtoList=manageService.getPostReport(order);
        try{
            return ApiResponse.onSuccess(postReportDtoList);
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),postReportDtoList);
        }
    }

    @DeleteMapping("/member")
    public ApiResponse deleteMember(@RequestParam("memberId") Long memberId)
    {
        try{
            manageService.deleteMember(memberId);
            return ApiResponse.onSuccess(null);
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),null);
        }
    }

    @DeleteMapping("/report/comment")
    public ApiResponse deleteCommentReport(@RequestParam(value="commentId") Long commentId)
    {
        try{
            manageService.deleteCommentReport(commentId);
            return ApiResponse.onSuccess(null);
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),null);
        }
    }

    @DeleteMapping("/report/sellPost")
    public ApiResponse deletePostReport(@RequestParam(value = "sellPostId") Long sellPostId)
    {
        try{
            manageService.deletePostReport(sellPostId);
            return ApiResponse.onSuccess(null);
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),null);
        }
    }
}
