package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    @Query(value="SELECT COUNT(*) FROM member WHERE DATE(created_at)=:day",nativeQuery = true)
    Long getCountByDate(@Param("day") LocalDate day);

    @Query(value="SELECT * FROM Member WHERE DATE(login_time) <:day ",nativeQuery = true)
    List<Member> findByLoginTime(@Param("day") LocalDate day);
}