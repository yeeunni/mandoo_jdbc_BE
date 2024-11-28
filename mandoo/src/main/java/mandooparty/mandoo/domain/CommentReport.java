package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;

// Removed: @Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// Removed: @Table(name = "commentReport")
public class CommentReport extends BaseEntity {

    // Removed: @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer commentReportCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;          // ìì±ì (Memberì ì°ê´ ê´ê³ ì¤ì )


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId", nullable = false)
    private Comment comment;        // ëê¸(comment)ìì ê´ê³
}
