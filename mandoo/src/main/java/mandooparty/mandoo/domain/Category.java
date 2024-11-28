package mandooparty.mandoo.domain;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long categoryId;  // 카테고리 ID
    private String name;      // 카테고리 이름

    public void setId(long categoryId) {
        this.categoryId = categoryId;
    }

}
