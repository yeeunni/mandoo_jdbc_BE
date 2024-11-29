package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentReportRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertCommentReport(CommentReport commentReport)
    {
        Integer postReportCount = commentReport.getComment_report_count();
        Long memberId=commentReport.getMember_id();
        Long commentId=commentReport.getComment_id();
        LocalDateTime createdAt=commentReport.getCreated_at();
        LocalDateTime updatedAt=commentReport.getUpdated_at();
        String sql = "INSERT INTO commentreport (comment_report_count, member_id,comment_id,created_at,updated_at) " +
                "VALUES (?, ?, ?, ?,?)";
        int rowsAffected = jdbcTemplate.update(sql, postReportCount, memberId, commentId, createdAt,updatedAt);
        return rowsAffected > 0;
    }

    public List<CommentReport> findAll(String order) {
        String sql = "SELECT c.* FROM commentreport AS c";
        if (order != null && (order.equalsIgnoreCase("name ASC") || order.equalsIgnoreCase("name DESC")
                || order.equalsIgnoreCase("login_time ASC") || order.equalsIgnoreCase("login_time DESC"))) {
            sql += " ORDER BY " + order;}
        // query 메서드를 사용하여 결과를 List<CommentReport>로 매핑
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CommentReport.class));
    }

    public Optional<CommentReport> findByCommentAndMember(Comment comment,Member member){
        String sql="SELECT c.* FROM commentreport AS c WHERE c.comment_id=? AND c.member_id=?";
        try {
            // queryForObject를 사용하여 하나의 결과를 반환
            CommentReport commentReport = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{comment.getId(), member.getId()},
                    new BeanPropertyRowMapper<>(CommentReport.class)
            );
            return Optional.ofNullable(commentReport); // like가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 결과가 없으면 Optional.empty() 반환
        }
    }


    public  CommentReport findByComment(Comment comment)
    {
        String sql="SELECT c.* FROM commentreport AS c WHERE c.comment_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{comment.getId()},CommentReport.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
