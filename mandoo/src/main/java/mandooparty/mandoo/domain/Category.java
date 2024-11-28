package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;

// Removed: @Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// Removed: @Table(name = "category")
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;     // ì¹´íê³ ë¦¬ ID

    private String name;         // ì¹´íê³ ë¦¬ ì´ë¦

    public void setId(long categoryId) {
    }
}
