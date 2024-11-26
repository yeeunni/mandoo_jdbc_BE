package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Comment;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value="SELECT COUNT(*) FROM Comment as c WHERE DATE(c.created_at)=:day;",nativeQuery = true)
    Long getCountByDate(@Param("day") LocalDate day);

//    @Query("DELETE FROM Comment as c WHERE c.id=:commentId")
//    void deleteById(Long commentId);
}
