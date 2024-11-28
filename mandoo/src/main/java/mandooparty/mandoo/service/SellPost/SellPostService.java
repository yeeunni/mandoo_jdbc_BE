package mandooparty.mandoo.service.SellPost;

import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public interface SellPostService {

    // ê²ìë¬¼ ìì±
    SellPostDTO.SellPostResponseDto SellPostcreate(SellPostDTO.SellPostCreateDto request);

    // ê²ìë¬¼ ì¡°í
    SellPostDTO.SellPostResponseDto getSellPostById(Long id);

    // ê²ìë¬¼ ì­ì 
    void deleteSellPost(Long id);
    // ê²ìë¬¼ ìë°ì´í¸
    SellPostDTO.SellPostResponseDto updateSellPost(Long postId, SellPostDTO.SellPostUpdateDto request);
    public Page<SellPostDTO.SellPostResponseDto> getRecentSellPosts (Pageable pageable);

}
