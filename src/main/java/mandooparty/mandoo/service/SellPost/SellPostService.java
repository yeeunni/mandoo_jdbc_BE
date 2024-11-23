package mandooparty.mandoo.service.SellPost;

import mandooparty.mandoo.web.dto.SellPostDTO;


public interface SellPostService {

    // 게시물 생성
    SellPostDTO.SellPostResponseDto SellPostcreate(SellPostDTO.SellPostCreateDto request);

    // 게시물 조회
    SellPostDTO.SellPostResponseDto getSellPostById(Long id);

    // 게시물 삭제
    void deleteSellPost(Long id);
    // 게시물 업데이트
    SellPostDTO.SellPostResponseDto updateSellPost(Long postId, SellPostDTO.SellPostUpdateDto request);
}
