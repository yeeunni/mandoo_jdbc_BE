package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "sellpostcategory")
public class SellPostCategory {

    private Long sellPostCategoryId;  // SellPostì Categoryì ì¤ê° íì´ë¸ ID

    private Category category;        // ì¹´íê³ ë¦¬ìì ê´ê³

    private SellPost sellPost;        // ê²ìë¬¼(SellPost)ìì ê´ê³


}
