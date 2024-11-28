package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.PostReport;
import mandooparty.mandoo.domain.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport,Long> {

    Optional<PostReport> findBySellPostAndMember(SellPost sellPost, Member member);

    PostReport findBySellPost(SellPost sellPost);
}
