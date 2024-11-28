package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;

// Removed: @Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "postReport")
public class PostReport extends BaseEntity {
    // Removed: @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer postReportCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;          // ìì±ì (Memberì ì°ê´ ê´ê³ ì¤ì )


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellpostId", nullable = false)
    private SellPost sellPost;        // ê²ìë¬¼ê³¼ì ê´ê³
}
