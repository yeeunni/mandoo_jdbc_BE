package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;

// Removed: @Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "likes")
public class Likes extends BaseEntity {
    // Removed: @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellpostId", nullable = false)
    private SellPost sellPost;


}
