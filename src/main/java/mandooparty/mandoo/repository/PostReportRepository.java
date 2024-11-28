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
        Integer postReportCount = postReport.getPostReportCount();
        Long memberId=postReport.getMember().getId();
        Long sellPostId=postReport.getSellPost().getSellPostId();
        LocalDateTime createdAt=postReport.getCreatedAt();
        LocalDateTime updatedAt=postReport.getUpdateAt();
        String sql = "INSERT INTO post_report (post_report_count, member_id,sellpost_id,created_at,updated_at) " +
                "VALUES (?, ?, ?, ?,?)";
        int rowsAffected = jdbcTemplate.update(sql, postReportCount, memberId, sellPostId, createdAt,updatedAt);
        return rowsAffected > 0;
    }


    public Optional<PostReport> findBySellPostAndMember(SellPost sellPost, Member member) {
        String sql = "SELECT p.* FROM PostReport as p WHERE p.sellpost_id=? and p.member_id=?";
        try {
            PostReport postReport = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPost.getSellPostId(), member.getId()},
                    new BeanPropertyRowMapper<>(PostReport.class)  // 수정된 부분
            );
            return Optional.ofNullable(postReport);  // Optional로 감싸서 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public PostReport findBySellPost(SellPost sellPost)
    {
        String sql="SELECT p.* FROM post_report p WHERE p.sellpost_id=?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPost.getSellPostId()},
                    new BeanPropertyRowMapper<>(PostReport.class)  // 수정된 부분
            );
        } catch (EmptyResultDataAccessException e) {
            return null;  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public List<PostReport> findAll(Sort sort)
    {
        String sql = "SELECT * FROM post_report";

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
