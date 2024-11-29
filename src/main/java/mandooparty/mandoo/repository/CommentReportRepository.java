package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentReportRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CommentReport insertCommentReport(CommentReport commentReport) {
        Integer postReportCount = commentReport.getComment_report_count();
        Long memberId = commentReport.getMember_id();
        Long commentId = commentReport.getComment_id();
        LocalDateTime createdAt = commentReport.getCreated_at();
        LocalDateTime updatedAt = commentReport.getUpdated_at();

        // KeyHolder 객체 생성
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // INSERT 쿼리
        String sql = "INSERT INTO commentreport (comment_report_count, member_id, comment_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        // PreparedStatementCreator를 사용하여 데이터 삽입 및 KeyHolder로 생성된 키를 받아옴
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postReportCount);
            ps.setLong(2, memberId);
            ps.setLong(3, commentId);
            ps.setTimestamp(4, Timestamp.valueOf(createdAt));
            ps.setTimestamp(5, Timestamp.valueOf(updatedAt));
            return ps;
        }, keyHolder);

        // 삽입 성공 시 자동 생성된 ID를 CommentReport 객체에 설정
        if (rowsAffected > 0) {
            commentReport.setId(keyHolder.getKey().longValue());  // 자동 생성된 ID 설정
        }

        // 삽입된 CommentReport 객체 반환
        return commentReport;
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
