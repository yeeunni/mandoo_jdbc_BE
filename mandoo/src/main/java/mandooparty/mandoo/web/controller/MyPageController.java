package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.SellPostConverter;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.MyPageService.MyPageService;
import mandooparty.mandoo.web.dto.MyPageDTO;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    @GetMapping("/like")//like한 게시물 조회
    public ApiResponse<List<SellPostDTO.SellPostResponseDto>> MyPageLike(@RequestBody MyPageDTO.MemberIdRequestDto request)
    {
        Long memberId=request.getMemberId();
        List<SellPost> sellPostList= myPageService.getLikeSellPost(memberId);//like한 게시물 get
        List<SellPostDTO.SellPostResponseDto> myPageResponseDtoList=new ArrayList<>();
        for(SellPost sellPost : sellPostList){//domain -> dto로 변경
            myPageResponseDtoList.add(SellPostConverter.sellPostResponseDto(sellPost));
        }
        try{
            return ApiResponse.onSuccess(myPageResponseDtoList);
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(), myPageResponseDtoList);
        }
    }

    @GetMapping("/sold")//판매완료한 게시물 조회
    public ApiResponse<List<SellPostDTO.SellPostResponseDto>> MyPageSold( @RequestBody MyPageDTO.MemberIdRequestDto request)
    {
        Long memberId=request.getMemberId();
        List<SellPost> sellPostList= myPageService.getSoldPost(memberId);//판매완료된 게시물 get
        List<SellPostDTO.SellPostResponseDto> myPageResponseDtoList=new ArrayList<>();
        for(SellPost sellPost : sellPostList){//domain -> dto로 변경
            myPageResponseDtoList.add(SellPostConverter.sellPostResponseDto(sellPost));
        }
        try{
            return ApiResponse.onSuccess(myPageResponseDtoList);
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(), myPageResponseDtoList);
        }
    }

    @GetMapping("/selling")//판매중인 게시물 조회
    public ApiResponse<List<SellPostDTO.SellPostResponseDto>> MyPageSelling( @RequestBody MyPageDTO.MemberIdRequestDto request)
    {
        Long memberId=request.getMemberId();
        List<SellPost> sellPostList= myPageService.getSellPost(memberId);//판매중인 게시물 get
        List<SellPostDTO.SellPostResponseDto> myPageResponseDtoList=new ArrayList<>();
        for(SellPost sellPost : sellPostList){//domain -> dto로 변경
            myPageResponseDtoList.add(SellPostConverter.sellPostResponseDto(sellPost));
        }
        try{
            return ApiResponse.onSuccess(myPageResponseDtoList);
        }catch(GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(), myPageResponseDtoList);
        }
    }
}
