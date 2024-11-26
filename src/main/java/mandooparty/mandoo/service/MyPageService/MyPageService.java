package mandooparty.mandoo.service.MyPageService;

import mandooparty.mandoo.domain.SellPost;

import java.util.List;

public interface MyPageService {
    public List<SellPost> getLikeSellPost(Long memberId);
    public List<SellPost> getSoldPost(Long memberId);
    public List<SellPost> getSellPost(Long memberId);
}
