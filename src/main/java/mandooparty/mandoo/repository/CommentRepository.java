package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Comment> findById(Long commentId)
    {
        String sql="SELECT c.* FROM comment AS c WHERE c.id=?";
        try {
            Comment comment= jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{commentId},
                    new BeanPropertyRowMapper<>(Comment.class)
            );
            return Optional.ofNullable(comment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }


    public boolean insertComment(Comment comment)
    {
        String content = comment.getContent();
        Integer status = comment.getComment_status();
        Long memberId = comment.getMember_id();
        Long parentCommentId = comment.getParent_comment_id();
        LocalDateTime createdAt=comment.getCreated_at();
        LocalDateTime updatedAt=comment.getUpdated_at();
        String sql = "INSERT INTO comment (content, comment_status, member_id, sellpost_id,parent_comment_id) " +
                "VALUES (?, ?, ?, ?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, content,status,memberId,parentCommentId,createdAt,updatedAt);
        return rowsAffected > 0;
    }

    public Long getCountByDate(LocalDate day)
    {
        String sql="SELECT COUNT(*) FROM comment as c WHERE DATE(c.created_at)=?";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.queryForObject(sql, new Object[]{sqlDate},Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Comment> findByParentComment(Comment parentComment) {
        String sql = "SELECT c.* FROM comment as c WHERE c.parent_comment_id=?";
        try {
            // parentComment의 ID를 추출하여 쿼리 실행
            return jdbcTemplate.query(sql, new Object[]{parentComment.getId()},
                    new BeanPropertyRowMapper<>(Comment.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

    public boolean deleteById(Long commentId) {
        String sql = "DELETE FROM comment WHERE id = ?"; // 테이블 및 열 이름을 스키마에 맞게 수정
        int rowsAffected = jdbcTemplate.update(sql, new Object[]{commentId});
        return rowsAffected > 0; // 영향을 받은 행이 있으면 true, 없으면 false 반환
    }
}
