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

    // 카테고리 ID 목록 (연결 테이블을 직접 처리하기 위해 간단한 리스트로 대체)
    private List<Long> category_ids;

    // 이미지 경로 목록 (이미지 파일 경로를 직접 저장)
    private List<String> image_paths;

    // 업데이트 메서드
    public void update(String title, Integer price, String description, String city, String gu, String dong, List<Long> categoryIds) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.city = city;
        this.gu = gu;
        this.dong = dong;
        this.category_ids = categoryIds; // 카테고리 ID 목록 업데이트
        this.updated_at = LocalDateTime.now(); // 수정 시간 갱신
    }
}
