package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.SellImagePath;
import mandooparty.mandoo.domain.SellPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SellImagePathRepository extends JpaRepository<SellImagePath, Long> {
}