package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.enums.CommentStatus;
import mandooparty.mandoo.domain.enums.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.cglib.core.Local;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Comment findById(Long commentId)
    {
        String sql="SELECT c.* FROM comment AS c WHERE c.id=?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{commentId},
                    new BeanPropertyRowMapper<>(Comment.class)  // 수정된 부분
            );
        } catch (EmptyResultDataAccessException e) {
            return null;  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public boolean insertComment(Comment comment)
    {
        String content = comment.getContent();
        CommentStatus status = comment.getCommentStatus();
        Long memberId = comment.getMember().getId();
        Long parentCommentId = comment.getParentComment().getId();
        LocalDateTime createdAt=comment.getCreatedAt();
        LocalDateTime updatedAt=comment.getUpdateAt();
        String sql = "INSERT INTO comment (content, comment_status, member_id, sellpost_id,parent_comment_id) " +
                "VALUES (?, ?, ?, ?,?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, content,status,memberId,parentCommentId,createdAt,updatedAt);
        return rowsAffected > 0;
    }

    public Integer getCountByDate(LocalDate day)
    {
        String sql="SELECT COUNT(*) FROM comment as c WHERE DATE(c.created_at)=?";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.queryForObject(sql, new Object[]{sqlDate},Integer.class);
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
