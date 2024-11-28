package mandooparty.mandoo.domain;

import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;
import mandooparty.mandoo.domain.enums.SellPostStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellPost extends BaseEntity {
    private Long sellPostId;

    private String title;           // ê²ìë¬¼ ì ëª©
    private Integer price;              // ìí ê°ê²©
    private String description;     // ê²ìê¸ ì¤ëª
    private SellPostStatus status;  // ê²ìê¸ ìí (ì: íë§¤ì¤, ê±°ëìë£ ë±)

    private Member member;          // ìì±ì (Memberì ì°ê´ ê´ê³ ì¤ì )
    private Integer commentCount = 0; // ì¡°íì

    private Integer likeCount = 0; // ì¢ìì ì

    private String city;    // íë§¤ì ì§ì­ ì£¼ì (ì)
    private String gu;      // íë§¤ì ì§ì­ ì£¼ì (êµ¬)
    private String dong;    // íë§¤ì ì§ì­ ì£¼ì (ë)

    private LocalDateTime createdAt = LocalDateTime.now(); // ìì± ì¼ì
    private LocalDateTime modifiedAt = LocalDateTime.now(); // ìì  ì¼ì
    private List<SellPostCategory> categories = new ArrayList<>(); // SellPostCategoryìì ì°ê´ ê´ê³ ì¤ì 
    private List<SellImagePath> images = new ArrayList<>(); // SellImagePathìì ì°ê´ ê´ê³ ì¤ì 
    private List<Likes> likes=new ArrayList<>();
    private List<Comment> comments=new ArrayList<>();
    private List<PostReport> postReports=new ArrayList<>();


    public Long getId() {
        return sellPostId;
    }

    public void setId(Long id) {
        this.sellPostId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return modifiedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.modifiedAt = updatedAt;
    }

    // ì¹´íê³ ë¦¬ ì¤ì  ë©ìë
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


    // ìë°ì´í¸ ë©ìë
    public void update(String title, Integer price, String description, String city, String gu, String dong, List<SellPostCategory> categories) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.city = city;
        this.gu = gu;
        this.dong = dong;
        setCategories(categories); // ì¹´íê³ ë¦¬ ë¦¬ì¤í¸ ìë°ì´í¸
        this.modifiedAt = LocalDateTime.now(); // ìì  ìê° ê°±ì 
    }


}
