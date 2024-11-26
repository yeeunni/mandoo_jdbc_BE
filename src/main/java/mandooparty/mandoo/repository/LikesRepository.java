package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Likes;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l.sellPost FROM Likes l WHERE l.member.id = :memberId")
    List<SellPost> findByMemberId(@Param("memberId") Long memberId);

    Optional<Likes> findBySellPostAndMember(SellPost sellPost, Member member);

    void deleteBySellPost(SellPost sellPost);
}
