package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Likes{

    private Long id;
    private Long member_id;
    private Long sell_post_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


}
