package mandooparty.mandoo.domain;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellPost {
    private Long sell_post_id;      // 게시물 ID
    private String title;         // 게시물 제목
    private Integer price;        // 상품 가격
    private String description;   // 게시글 설명
    private Integer status;        // 게시글 상태 (예: 판매중:0, 거래완료:1)

    private Long member_id;        // 작성자 ID (Member와의 연관 제거)
    private Integer comment_count; // 댓글 수
    private Integer like_count;    // 좋아요 수

    private String city;          // 판매자 지역 주소 (시)
    private String gu;            // 판매자 지역 주소 (구)
    private String dong;          // 판매자 지역 주소 (동)

    private LocalDateTime created_at;  // 생성 일자
    private LocalDateTime updated_at; // 수정 일자

}
