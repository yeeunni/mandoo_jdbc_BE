package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.SellPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import java.util.Optional;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {
    // 최신순으로 게시글 조회
    Page<SellPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

