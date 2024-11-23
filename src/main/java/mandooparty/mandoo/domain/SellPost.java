package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;
import mandooparty.mandoo.domain.enums.SellPostStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "sellpost")
public class SellPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellPostId;

    private String title;           // 게시물 제목
    private int price;              // 상품 가격
    private String description;     // 게시글 설명

    @Enumerated(EnumType.STRING)
    private SellPostStatus status;  // 게시글 상태 (예: 판매중, 거래완료 등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;          // 작성자 (Member와 연관 관계 설정)

    @Builder.Default
    private Integer commentCount = 0; // 조회수

    @Builder.Default
    private Integer likeCount = 0; // 좋아요 수

    private String city;    // 판매자 지역 주소 (시)
    private String gu;      // 판매자 지역 주소 (구)
    private String dong;    // 판매자 지역 주소 (동)

    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 일자
    private LocalDateTime modifiedAt = LocalDateTime.now(); // 수정 일자

    @OneToMany(mappedBy = "sellPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellPostCategory> categories = new ArrayList<>(); // SellPostCategory와의 연관 관계 설정

    @OneToMany(mappedBy = "sellPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellImagePath> images = new ArrayList<>(); // SellImagePath와의 연관 관계 설정

    // 카테고리 설정 메서드
    public void setCategories(List<SellPostCategory> categories) {
        this.categories.clear();
        System.out.println("SellPost Entity Category: " + categories);
        for (SellPostCategory category : categories) {
            category.setSellPost(this);
            this.categories.add(category);
        }
    }

    public void setImages(List<SellImagePath> images) {
        this.images.clear();

        System.out.println("SellPost Entity Image: " + images);
        for (SellImagePath image : images) {
            image.setSellPost(this);
            this.images.add(image);
        }
    }

    public void removeImage(SellImagePath imagePath) {
        images.remove(imagePath);
        imagePath.setSellPost(null);
    }



    // 업데이트 메서드
    public void update(String title, int price, String description, String city, String gu, String dong, List<SellPostCategory> categories, List<SellImagePath> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.city = city;
        this.gu = gu;
        this.dong = dong;
        setCategories(categories); // 카테고리 리스트 업데이트
        setImages(images);
        this.modifiedAt = LocalDateTime.now(); // 수정 시간 갱신
    }


}
