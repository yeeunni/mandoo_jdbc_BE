package mandooparty.mandoo.service.MyPageService;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.enums.SellPostStatus;
import mandooparty.mandoo.repository.LikesRepository;
import mandooparty.mandoo.repository.SellPostRepository;
import mandooparty.mandoo.service.Memberserivce.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final LikesRepository likesRepository;
    private final SellPostRepository sellPostRepository;
    public List<SellPost> getLikeSellPost(Long memberId)
    {
        return likesRepository.findByMemberId(memberId);//ìì íì + page ì¬ì©í´ì¼í¨
    }

    public List<SellPost> getSoldPost(Long memberId)
    {
        return sellPostRepository.findByMemberAndStatus(memberId,SellPostStatus.SOLD_OUT);//ìì íì + page ì¬ì©í´ì¼í¨
    }

    public List<SellPost> getSellPost(Long memberId)
    {
        return sellPostRepository.findByMemberAndStatus(memberId,SellPostStatus.FOR_SALE);//ìì íì + page ì¬ì©í´ì¼í¨
    }

}
