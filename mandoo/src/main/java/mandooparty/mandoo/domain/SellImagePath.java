package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellImagePath {
    private Long imageId;
    private SellPost sellPost;
    private String path; // ì´ë¯¸ì§ ê²½ë¡

}