package mandooparty.mandoo.web.controller;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.apiPayload.ApiResponse;
import mandooparty.mandoo.converter.LikesConverter;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.service.LikesService.LikesService;
import mandooparty.mandoo.web.dto.LikesDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikesController {

    private final LikesService likesService;
    @PostMapping("/{sellPostId}")
    public ApiResponse<LikesDTO.LikesReponseDto> createLike(
            @PathVariable("sellPostId") Long sellPostId,
            @RequestParam("memberId") Long memberId)
    {
        try{
            return ApiResponse.onSuccess(LikesConverter.likesReponseDto(likesService.createLikes(sellPostId,memberId)));
        }catch (GlobalException e){
            return ApiResponse.onFailure(e.getErrorCode(),LikesConverter.likesReponseDto(likesService.createLikes(sellPostId,memberId)));
        }
    }
}
