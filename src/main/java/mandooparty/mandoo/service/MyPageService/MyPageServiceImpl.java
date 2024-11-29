package mandooparty.mandoo.service.MyPageService;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.domain.SellPost;
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
        return likesRepository.findByMemberId(memberId);//수정필요 + page 사용해야함
    }

    public List<SellPost> getSoldPost(Long memberId)
    {
        return sellPostRepository.findByMemberAndStatus(memberId,1);//수정필요 + page 사용해야함
    }

    public List<SellPost> getSellPost(Long memberId)
    {
        return sellPostRepository.findByMemberAndStatus(memberId,0);//수정필요 + page 사용해야함
    }

}
