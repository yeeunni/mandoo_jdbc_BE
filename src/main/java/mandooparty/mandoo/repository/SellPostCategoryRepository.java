package mandooparty.mandoo.repository;

import jakarta.persistence.Tuple;
import mandooparty.mandoo.domain.SellPostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellPostCategoryRepository extends JpaRepository<SellPostCategory, Long> {

    @Query("SELECT c.name, COUNT(sp) " +
            "FROM SellPostCategory sp JOIN sp.category c " +
            "GROUP BY c.id, c.name")
    List<Tuple> countCategory();
}