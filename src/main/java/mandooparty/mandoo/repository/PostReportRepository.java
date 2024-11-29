package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.PostReport;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.enums.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostReportRepository {  // 수정: class 키워드 추가

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertPostReport(PostReport postReport)
    {
        Integer postReportCount = postReport.getPost_report_count();
        Long memberId=postReport.getMember_id();
        Long sellPostId=postReport.getSell_post_id();
        LocalDateTime createdAt=postReport.getCreated_at();
        LocalDateTime updatedAt=postReport.getUpdated_at();
        String sql = "INSERT INTO postreport (post_report_count, member_id,sell_post_id,created_at,updated_at) " +
                "VALUES (?, ?, ?, ?,?)";
        int rowsAffected = jdbcTemplate.update(sql, postReportCount, memberId, sellPostId, createdAt,updatedAt);
        return rowsAffected > 0;
    }


    public Optional<PostReport> findBySellPostAndMember(SellPost sellPost, Member member) {
        String sql = "SELECT p.* FROM postreport as p WHERE p.sell_post_id=? and p.member_id=?";
        try {
            PostReport postReport = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPost.getSell_post_id(), member.getId()},
                    new BeanPropertyRowMapper<>(PostReport.class)  // 수정된 부분
            );
            return Optional.ofNullable(postReport);  // Optional로 감싸서 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public PostReport findBySellPost(SellPost sellPost)
    {
        String sql="SELECT p.* FROM postreport p WHERE p.sell_post_id=?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPost.getSell_post_id()},
                    new BeanPropertyRowMapper<>(PostReport.class)  // 수정된 부분
            );
        } catch (EmptyResultDataAccessException e) {
            return null;  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public List<PostReport> findAll(Sort sort)
    {
        String sql = "SELECT * FROM postreport";

        // Add sorting logic
        if (sort != null && sort.isSorted()) {
            StringBuilder orderByClause = new StringBuilder(" ORDER BY ");
            sort.forEach(order -> {
                orderByClause.append(order.getProperty())  // Add the column to sort by
                        .append(" ")
                        .append(order.getDirection())  // Add the sorting direction (ASC/DESC)
                        .append(", ");
            });
            // Remove the trailing comma and space
            sql += orderByClause.substring(0, orderByClause.length() - 2);
        }

        // Execute the query and map the result to a list of PostReport objects
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PostReport.class));
    }


}
