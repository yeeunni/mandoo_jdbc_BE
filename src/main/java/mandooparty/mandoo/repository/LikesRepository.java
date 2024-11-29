package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Likes;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.enums.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class LikesRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<SellPost> findByMemberId(Long memberId)
    {
        String sql="SELECT l.sellPost FROM Likes as l WHERE l.member_id = ?";try {
        // parentComment의 ID를 추출하여 쿼리 실행
        return jdbcTemplate.query(sql, new Object[]{memberId},
                new BeanPropertyRowMapper<>(SellPost.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

    public boolean insertLikes(Likes likes)
    {
        Long memberId = likes.getMember_id();
        Long sellPostId=likes.getSell_post_id();
        LocalDateTime createdAt=likes.getCreated_at();
        LocalDateTime updatedAt=likes.getUpdated_at();
        String sql = "INSERT INTO likes (member_id, sell_post_id,created_at,updated_at) " +
                "VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, memberId, sellPostId, createdAt, updatedAt);
        return rowsAffected > 0;
    }

    public Optional<Likes> findBySellPostAndMember(SellPost sellPost, Member member) {
        String sql = "SELECT l.* FROM Likes as l WHERE l.sellpost_id=? AND l.member_id=?";
        try {
            // queryForObject를 사용하여 하나의 결과를 반환
            Likes like = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPost.getSell_post_id(), member.getId()},
                    new BeanPropertyRowMapper<>(Likes.class)
            );
            return Optional.ofNullable(like); // like가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 결과가 없으면 Optional.empty() 반환
        }
    }

    public boolean deleteBySellPost(SellPost sellPost) {
        String sql = "DELETE FROM Likes WHERE sellpost_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, sellPost.getSell_post_id());
        return rowsAffected > 0;  // 영향을 받은 행이 있으면 true, 없으면 false 반환
    }


}
