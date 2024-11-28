package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;
import mandooparty.mandoo.domain.enums.CommentStatus;

import java.util.ArrayList;
import java.util.List;

// Removed: @Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "comment")
public class Comment extends BaseEntity {

    // Removed: @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;

    @Setter
    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;          // ìì±ì (Memberì ì°ê´ ê´ê³ ì¤ì )


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellpostId", nullable = false)
    private SellPost sellPost;        // ê²ìë¬¼(SellPost)ìì ê´ê³

    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CommentReport> commentReports=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentCommentId") // ë¶ëª¨ ëê¸ ID ì ì¥
    private Comment parentComment; // ë¶ëª¨ ëê¸ (ëµê¸ì ê²½ì°)

}
