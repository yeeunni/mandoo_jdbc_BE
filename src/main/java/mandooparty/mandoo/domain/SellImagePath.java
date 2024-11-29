package mandooparty.mandoo.domain;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellImagePath {
    private Long image_id;
    private Long sell_post_id;
    private String path;

}