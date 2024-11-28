package mandooparty.mandoo.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "sellpostcategory")
public class SellPostCategory {

    private Long sell_post_category_id;  // SellPostì Categoryì ì¤ê° íì´ë¸ ID

    private Long category_id;        // ì¹´íê³ ë¦¬ìì ê´ê³

    private Long sell_post_id;        // ê²ìë¬¼(SellPost)ìì ê´ê³


}