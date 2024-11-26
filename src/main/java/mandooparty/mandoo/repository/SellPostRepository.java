package mandooparty.mandoo.repository;

import jakarta.persistence.Tuple;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.enums.SellPostStatus;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import java.util.Optional;

@Repository
public interface SellPostRepository extends JpaRepository<SellPost, Long> {

    Optional<SellPost> findByTitle(String title);

    @Query("SELECT s FROM SellPost s WHERE s.member.id = :memberId AND s.status= :status")
    List<SellPost> findByMemberAndStatus(@Param("memberId") Long memberId,@Param("status") SellPostStatus status);

    Optional<SellPost> existsByMemberId(Long memberId);


    @Query("SELECT FUNCTION('DATE', s.createdAt) AS createDate, COUNT(s) " +
            "FROM SellPost s " +
            "GROUP BY FUNCTION('DATE', s.createdAt) " +
            "ORDER BY FUNCTION('DATE', s.createdAt) ASC")
    List<Tuple> countByCreatedAt();


    @Query(value="SELECT COUNT(*) FROM SellPost as s WHERE DATE(s.created_at)=:day",nativeQuery = true)
    Long getCountByDate(@Param("day") LocalDate day);

    // 최신순으로 게시글 조회
    Page<SellPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

}

