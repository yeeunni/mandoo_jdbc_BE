package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageImpl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

@Repository
public class SellPostRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<SellPost> findById(Long sellPostId)
    {
        String sql="SELECT s.* FROM sellpost AS s WHERE s.sell_post_id=?";
        try {
            SellPost sellPost= jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPostId},
                    new BeanPropertyRowMapper<>(SellPost.class)  // 수정된 부분
            );
            return Optional.ofNullable(sellPost);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public boolean insertSellPost(SellPost sellPost) {
        String sql = "INSERT INTO sellpost (title, price, description, city, gu, dong, member_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sellPost.getTitle());
            ps.setInt(2, sellPost.getPrice());
            ps.setString(3, sellPost.getDescription());
            ps.setString(4, sellPost.getCity());
            ps.setString(5, sellPost.getGu());
            ps.setString(6, sellPost.getDong());
            ps.setLong(7, sellPost.getMember_id());
            ps.setTimestamp(8, Timestamp.valueOf(sellPost.getCreated_at()));
            ps.setTimestamp(9, Timestamp.valueOf(sellPost.getUpdated_at()));
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            sellPost.setSell_post_id(keyHolder.getKey().longValue());
            return true;
        }
        return false;
    }


    public boolean insertSellPostCategory(Long sellPostId, Long categoryId) {
        String sql = "INSERT INTO sellpostCategory (sell_post_id, category_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, sellPostId, categoryId);
        return rowsAffected > 0;
    }


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

    public List<SellPost> findByMemberAndStatus(Long memberId,Integer sellPostStatus)
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
                result.put("count", rs.getInt("count"));
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
                                .sell_post_id(rs.getLong("id"))
                                .created_at(rs.getTimestamp("created_at").toLocalDateTime())
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


    public Optional<SellPost> existsById(Long sellPostId)
    {
        String sql = "SELECT s.* FROM sellpost AS s WHERE s.sell_post_id = ?";
        try {
            SellPost sellPost = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{sellPostId},
                    new BeanPropertyRowMapper<>(SellPost.class)
            );
            return Optional.ofNullable(sellPost);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }
    public boolean deleteById(Long sellPostId) {
        String sql = "DELETE FROM sellpost WHERE sell_post_id = ?"; // 테이블 및 열 이름을 스키마에 맞게 수정
        int rowsAffected = jdbcTemplate.update(sql, new Object[]{sellPostId});
        return rowsAffected > 0; // 영향을 받은 행이 있으면 true, 없으면 false 반환
    }


    public boolean deleteBySellPost(SellPost sellPost) {
        String sql = "DELETE FROM sellpost AS s WHERE s.sell_post_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, sellPost.getSell_post_id());
        return rowsAffected > 0;  // 영향을 받은 행이 있으면 true, 없으면 false 반환
    }

}

