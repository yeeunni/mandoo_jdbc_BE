package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Likes;
import mandooparty.mandoo.web.dto.LikesDTO;
import org.springframework.stereotype.Component;

@Component
public class LikesConverter {

    public static LikesDTO.LikesReponseDto likesReponseDto(Likes likes){
        return LikesDTO.LikesReponseDto.builder()
                .likesId(likes.getId())
                .memberId(likes.getMember_id())
                .sellPostId(likes.getSell_post_id())
                .build();
    }
}
