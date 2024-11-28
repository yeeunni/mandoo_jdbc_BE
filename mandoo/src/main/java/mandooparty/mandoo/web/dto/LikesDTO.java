package mandooparty.mandoo.web.dto;

import lombok.*;

@Data
public class LikesDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikesReponseDto{
        private Long likesId;
        private Long memberId;
        private Long sellPostId;
    }
}
