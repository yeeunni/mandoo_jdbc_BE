package mandooparty.mandoo.service.LikesService;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.domain.Likes;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.LikesRepository;
import mandooparty.mandoo.repository.MemberRepository;
import mandooparty.mandoo.repository.SellPostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
    private final LikesRepository likesRepository;
    private final SellPostRepository sellPostRepository;
    private final MemberRepository memberRepository;
    public Likes createLikes(Long sellPostId,Long memberId) {
        Optional<SellPost> findSellPost=sellPostRepository.findById(sellPostId);
        Optional<Member> findMember=memberRepository.findById(memberId);
        if(findMember.isEmpty()){
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }else if(findSellPost.isEmpty()){
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }else{
            SellPost sellPost=findSellPost.get();
            Member member=findMember.get();
            Optional<Likes> existLikes=likesRepository.findBySellPostAndMember(sellPost,member);
            if(existLikes.isPresent()){
                throw new GlobalException(GlobalErrorCode.DUPLICATE_LIKES);
            }else{
                Likes likes=Likes.builder()
                        .member_id(member.getId())
                        .sell_post_id(sellPost.getSellPostId())
                        .build();
                likesRepository.insertLikes(likes);
                return likes;
            }
        }
    }
}
