package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.Category;
import mandooparty.mandoo.domain.SellPostCategory;
import mandooparty.mandoo.domain.SellImagePath;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellPostConverter {

    // SellPostCreateDto -> SellPost ìí°í°ë¡ ë³í
    public SellPost sellPostCreateDto(SellPostDTO.SellPostCreateDto dto, Member member) {
        List<SellImagePath> imagePaths = dto.getImages().stream()
                .map(file -> SellImagePath.builder()
                        .path(file.getOriginalFilename()) // ì¤ì  íì¼ ì ì¥ í ê²½ë¡ ì¤ì ì´ íìí¨
                        .build())
                .collect(Collectors.toList());

        return SellPost.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .city(dto.getCity())
                .gu(dto.getGu())
                .dong(dto.getDong())
                .member(member)
                .createdAt(LocalDateTime.now())     // ìì±ì¼ì ì¤ì 
                .modifiedAt(LocalDateTime.now())   // ìì ì¼ì ì¤ì 
                .categories(new ArrayList<>())
                .images(imagePaths)
                .build();
    }

    // SellPost -> SellPostResponseDto ë³í
    public static SellPostDTO.SellPostResponseDto sellPostResponseDto(SellPost sellPost) {
        return SellPostDTO.SellPostResponseDto.builder()
                .sellPostId(sellPost.getSellPostId())   // ê²ìê¸ ID ì¤ì 
                .title(sellPost.getTitle())             // ê²ìê¸ ì ëª© ì¤ì 
                .price(sellPost.getPrice())             // ê°ê²© ì¤ì 
                .description(sellPost.getDescription()) // ê²ìê¸ ì¤ëª ì¤ì 
                .city(sellPost.getCity())               // ê²ìë¬¼ ìì¹ - ì ì¤ì 
                .gu(sellPost.getGu())                   // ê²ìë¬¼ ìì¹ - êµ¬ ì¤ì 
                .dong(sellPost.getDong())               // ê²ìë¬¼ ìì¹ - ë ì¤ì 
                .likeCount(sellPost.getLikeCount())     // ì¢ìì ì ì¤ì 
                .commentCount(sellPost.getCommentCount()) // ëê¸ ì ì¤ì 
                .createdAt(sellPost.getCreatedAt())     // ìì±ì¼ì ì¤ì 
                .modifiedAt(sellPost.getModifiedAt())   // ìì ì¼ì ì¤ì 
                .memberId(sellPost.getMember() != null ? sellPost.getMember().getId() : null) // memberId ì¤ì 
                .categories(sellPost.getCategories().stream()  // ì¹´íê³ ë¦¬ ëª©ë¡ ì¤ì 
                        .map(SellPostCategory::getCategory)    // SellPostCategoryìì Category ê°ì²´ ì¶ì¶
                        .map(Category::getName)                // Category ê°ì²´ìì ì´ë¦ ì¶ì¶
                        .collect(Collectors.toList()))         // ì´ë¦ì ë¦¬ì¤í¸ë¡ ìì§íì¬ ë°í
                .images(sellPost.getImages().stream()          // ì´ë¯¸ì§ ëª©ë¡ ì¤ì 
                        .map(SellImagePath::getPath)           // SellImagePath ê°ì²´ìì ê²½ë¡ ì¶ì¶
                        .collect(Collectors.toList()))         // ê²½ë¡ë¥¼ ë¦¬ì¤í¸ë¡ ìì§íì¬ ë°í
                .build();
    }


    public static SellPostDTO.SellPostResponseDto sellPostGetResponse(SellPost sellPost) {
        return SellPostDTO.SellPostResponseDto.builder()
                .sellPostId(sellPost.getSellPostId())   // ê²ìê¸ ID ì¤ì 
                .title(sellPost.getTitle())             // ê²ìê¸ ì ëª© ì¤ì 
                .price(sellPost.getPrice())             // ê°ê²© ì¤ì 
                .description(sellPost.getDescription()) // ê²ìê¸ ì¤ëª ì¤ì 
                .city(sellPost.getCity())               // ê²ìë¬¼ ìì¹ - ì ì¤ì 
                .gu(sellPost.getGu())                   // ê²ìë¬¼ ìì¹ - êµ¬ ì¤ì 
                .dong(sellPost.getDong())               // ê²ìë¬¼ ìì¹ - ë ì¤ì 
                .likeCount(sellPost.getLikeCount())     // ì¢ìì ì ì¤ì 
                .commentCount(sellPost.getCommentCount()) // ëê¸ ì ì¤ì 
                .createdAt(sellPost.getCreatedAt())     // ìì±ì¼ì ì¤ì 
                .modifiedAt(sellPost.getModifiedAt())   // ìì ì¼ì ì¤ì 
                .memberId(sellPost.getMember() != null ? sellPost.getMember().getId() : null) // memberId ì¤ì 
                .categories(sellPost.getCategories().stream()  // ì¹´íê³ ë¦¬ ëª©ë¡ ì¤ì 
                        .map(SellPostCategory::getCategory)    // SellPostCategoryìì Category ê°ì²´ ì¶ì¶
                        .map(Category::getName)                // Category ê°ì²´ìì ì´ë¦ ì¶ì¶
                        .collect(Collectors.toList()))         // ì´ë¦ì ë¦¬ì¤í¸ë¡ ìì§íì¬ ë°í
                .images(sellPost.getImages().stream()          // ì´ë¯¸ì§ ëª©ë¡ ì¤ì 
                        .map(SellImagePath::getPath)           // SellImagePath ê°ì²´ìì ê²½ë¡ ì¶ì¶
                        .collect(Collectors.toList()))         // ê²½ë¡ë¥¼ ë¦¬ì¤í¸ë¡ ìì§íì¬ ë°í
                .build();
    }
}

