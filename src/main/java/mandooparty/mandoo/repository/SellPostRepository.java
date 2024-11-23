package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {
}

