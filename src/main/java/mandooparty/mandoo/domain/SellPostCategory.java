package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "sellpostcategory")
public class SellPostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellPostCategoryId;  // SellPost와 Category의 중간 테이블 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;        // 카테고리와의 관계

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellpostId", nullable = false)
    private SellPost sellPost;        // 게시물(SellPost)와의 관계

}
