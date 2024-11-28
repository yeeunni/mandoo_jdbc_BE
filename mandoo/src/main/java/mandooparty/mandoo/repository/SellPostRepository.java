package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.enums.SellPostStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

@Repository
public class SellPostRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Optional<SellPost> findByTitle(String title)
    {
        String sql="SELECT s.* FROM sellpost AS s WHERE s.title=?";
        try {
            SellPost sellPost = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{title},
                    new BeanPropertyRowMapper<>(SellPost.class)
            );
            return Optional.ofNullable(sellPost);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public List<SellPost> findByMemberAndStatus(Long memberId,SellPostStatus sellPostStatus)
    {
        String sql="SELECT s.* FROM sellpost  AS s WHERE s.member_id = ? AND s.status= ?";
        try {

            return jdbcTemplate.query(sql, new Object[]{memberId,sellPostStatus},
                    new BeanPropertyRowMapper<>(SellPost.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과가 없으면 null 반환
        }
    }

    public Optional<SellPost> existsByMemberId(Long memberId)
    {
        String sql = "SELECT s.* FROM sellpost AS s WHERE s.member_id = ?";
        try {
            SellPost sellPost = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{memberId},
                    new BeanPropertyRowMapper<>(SellPost.class)
            );
            return Optional.ofNullable(sellPost);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }


//    Optional<SellPost> existsByMemberId(Long memberId);

    public List<Map<String, Object>> countByCreatedAt() {
        String sql = "SELECT DATE(s.created_at) AS createDate, COUNT(*) AS count " +
                "FROM sellpost s " +
                "GROUP BY DATE(s.created_at) " +
                "ORDER BY DATE(s.created_at) ASC";

        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<>();
                result.put("createDate", rs.getDate("createDate"));
                result.put("count", rs.getInt("COUNT(s)"));
                return result;
            }
        });
    }

//    @Query("SELECT FUNCTION('DATE', s.createdAt) AS createDate, COUNT(s) " +
//            "FROM SellPost s " +
//            "GROUP BY FUNCTION('DATE', s.createdAt) " +
//            "ORDER BY FUNCTION('DATE', s.createdAt) ASC")
//    List<Tuple> countByCreatedAt();

    public Long getCountByDate(LocalDate day)
    {
        String sql="SELECT COUNT(*) FROM sellpost as s WHERE DATE(s.created_at)=?";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.queryForObject(sql, new Object[]{sqlDate},Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
//    @Query(value="SELECT COUNT(*) FROM SellPost as s WHERE DATE(s.created_at)=:day",nativeQuery = true)
//    Long getCountByDate(@Param("day") LocalDate day);


    Page<SellPost> findAllByOrderByCreatedAtDesc(Pageable pageable)
    {
        String sql = "SELECT * FROM sellpost s ORDER BY s.created_at DESC LIMIT ? OFFSET ?";

        // 전체 레코드 수를 구하는 쿼리
        String countSql = "SELECT COUNT(*) FROM sellpost";

        // OFFSET 계산 (page * size)
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        // 전체 레코드 수 조회
        int totalRecords = jdbcTemplate.queryForObject(countSql, Integer.class);

        // sellpost 데이터를 조회 (페이징)
        List<SellPost> sellPosts = jdbcTemplate.query(sql, new Object[]{limit, offset}, new RowMapper<SellPost>() {
            @Override
            public SellPost mapRow(ResultSet rs, int rowNum) throws SQLException {
                SellPost sellPost = SellPost.builder()
                        .sellPostId(rs.getLong("id"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .build();
                // 다른 필드들도 셋팅
                return sellPost;
            }
        });

        // Page 객체 생성
        return new PageImpl<>(sellPosts, pageable, totalRecords);
    }


}

