package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.SellPost.CategoryService;
import mandooparty.mandoo.service.SellPost.SellPostService;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellpost")
public class SellPostController {

    private final SellPostService sellPostService;
    private final CategoryService categoryService;

    @PostMapping("/write")
    public ApiResponse<SellPostDTO.SellPostResponseDto> createSellPost(@ModelAttribute SellPostDTO.SellPostCreateDto request) {
        try {

            SellPostDTO.SellPostResponseDto responseDto = sellPostService.SellPostcreate(request);
            return ApiResponse.onSuccess(responseDto);
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), null);
        }
    }


    @GetMapping("/write")
    public ApiResponse<SellPostDTO.SellPostWritePageDto> getSellPostWritePage() {
        // 모든 카테고리 이름을 문자열 리스트로 가져옵니다.
        List<String> categoryNames = categoryService.getAllCategory();
        SellPostDTO.SellPostWritePageDto response = new SellPostDTO.SellPostWritePageDto(categoryNames);
        return ApiResponse.onSuccess(response);
    }


    // 게시글 조회 요청 처리
    @GetMapping("/read/{sellPostId}")
    public ApiResponse<SellPostDTO.SellPostResponseDto> getSellPost(@PathVariable("sellPostId") Long sellPostId) {
        try {
            return ApiResponse.onSuccess(sellPostService.getSellPostById(sellPostId));
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), null);
        }
    }

    @PutMapping("/update/{sellPostId}")
    public ApiResponse<SellPostDTO.SellPostResponseDto> updateSellPost(
            @PathVariable("sellPostId") Long sellPostId,
            @ModelAttribute SellPostDTO.SellPostUpdateDto request) { // 요청으로 userId 받기 (로그인된 사용자 ID를 포함)
        try {
            SellPostDTO.SellPostResponseDto responseDto = sellPostService.updateSellPost(sellPostId, request);
            return ApiResponse.onSuccess(responseDto);
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), null);
        }
    }

    @GetMapping("/update/{sellPostId}")
    public ApiResponse<SellPostDTO.SellPostUpdatePageDto> getSellPostUpdatePage() {
        // 모든 카테고리 이름을 문자열 리스트로 가져옵니다.
        List<String> categoryNames = categoryService.getAllCategory();
        SellPostDTO.SellPostUpdatePageDto response = new SellPostDTO.SellPostUpdatePageDto(categoryNames);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/recent") //최신 게시물 조회
    public ApiResponse<Page<SellPostDTO.SellPostResponseDto>> getSellPosts(@RequestParam("page") int page) {
        try {
            int pageSize = 9; // 한 페이지에 가져올 게시글 수
            PageRequest pageRequest = PageRequest.of(page, pageSize);
            return ApiResponse.onSuccess(sellPostService.getRecentSellPosts(pageRequest));
        } catch (GlobalException e) {
            return ApiResponse.onFailure(e.getErrorCode(), null);
        }

    }


}