package mandooparty.mandoo.service.LikesService;


import mandooparty.mandoo.domain.Likes;

public interface LikesService {

    public Likes createLikes(Long sellPostId,Long memberId);
}
