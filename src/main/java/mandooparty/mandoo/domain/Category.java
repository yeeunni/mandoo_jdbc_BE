package mandooparty.mandoo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long category_id;  // 카테고리 ID
    private String name;      // 카테고리 이름

}