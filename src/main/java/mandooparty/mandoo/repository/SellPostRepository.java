package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.web.dto.SellPostDTO;
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
    public Optional<SellPostDTO.SellPostResponseWithLikeDto> findByIdWithMemberId(Long sellPostId, Long memberId)
    {
        String sql="SELECT s.*,\n" +
                "si.path,\n"+
                "c.member_id AS comment_member_id,\n"+
        "c.created_at AS comment_created_at,\n"+
            "c.updated_at AS comment_updated_at,\n"+
        "c.content,c.comment_status, c.parent_comment_id\n"+
                "\tCASE \n" +
                "        WHEN l.member_id IS NOT NULL THEN true\n" +
                "        ELSE false\n" +
                "    END AS is_liked\n" +
                "FROM sellpost AS s\n" +
                "LEFT JOIN likes l \n" +
                "    ON s.sell_post_id = l.sell_post_id AND l.member_id = ?\n" +
                "LEFT JOIN sellimagepath AS si \n" +
                "    ON s.sell_post_id=si.sell_post_id\n" +
                "WHERE s.sell_post_id=?";
        try {
            SellPostDTO.SellPostResponseWithLikeDto sellPost= jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{memberId,sellPostId},
                    sellPostWithLikeRowMapper  // 수정된 부분
            );
            return Optional.ofNullable(sellPost);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }

    public SellPost insertSellPost(SellPost sellPost) {
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
            // 키값을 받아서 sellPost 객체에 설정
            sellPost.setSell_post_id(keyHolder.getKey().longValue());
            sellPost.setComment_count(0);
            sellPost.setLike_count(0);
            return sellPost; // 삽입된 SellPost 객체를 반환
        }

        return null; // 삽입 실패시 null 반환
    }

    public List<String> findCategoriesBySellPostId(Long sellPostId) {
        String sql = "SELECT c.name FROM category c " +
                "JOIN sellpostcategory sc ON c.category_id = sc.category_id " +
                "WHERE sc.sellpost_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{sellPostId}, String.class);

    }

    public List<String> findImagesBySellPostId(Long sellPostId) {
        String sql = "SELECT path FROM sellimagepath WHERE sellpost_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{sellPostId}, String.class);
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

    public List<SellPostDTO.SellPostResponseWithLikeDto> searchByKeyword(Pageable pageable, String keyword,Long memberId) {
        // OFFSET 계산 (page * size)
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int limit = pageable.getPageSize();

        // SQL에 LIMIT과 OFFSET 반영
        String sql = "SELECT s.*,\n"+
                "si.path,\n"+
        "CASE\n"+
        "WHEN l.member_id IS NOT NULL THEN true\n"+
        "ELSE false\n"+
        "END AS is_liked\n"+
        "FROM sellpost AS s\n"+
        "LEFT JOIN likes l\n"+
        "ON s.sell_post_id = l.sell_post_id AND l.member_id = ?\n"+
                "LEFT JOIN sellimagepath as si\n"+
                "ON s.sell_post_id=si.sell_post_id\n"+
        "WHERE MATCH(s.title) AGAINST(?)\n"+
        "LIMIT ? OFFSET ?";

        // keyword, limit, offset 순서로 바인딩
        List<SellPostDTO.SellPostResponseWithLikeDto> sellPosts = jdbcTemplate.query(sql, new Object[]{memberId,keyword, limit, offset}, sellPostWithLikeRowMapper);

        return sellPosts;
    }

    private final RowMapper<SellPostDTO.SellPostResponseWithLikeDto> sellPostWithLikeRowMapper = (rs, rowNum) -> {
        SellPostDTO.SellPostResponseWithLikeDto dto=new SellPostDTO.SellPostResponseWithLikeDto();
        dto.setSellPostId(rs.getLong("sell_post_id"));
        dto.setTitle(rs.getString("title"));
        dto.setDescription(rs.getString("description"));
        dto.setPrice(rs.getInt("price"));
        dto.setCity(rs.getString("city"));
        dto.setMemberId(rs.getLong("member_id"));
        dto.setLikeCount(rs.getInt("like_count"));
        dto.setCommentCount(rs.getInt("comment_count"));
        dto.setGu(rs.getString("gu"));
        dto.setStatus(rs.getInt("status"));
        dto.setDong(rs.getString("dong"));
        dto.setImages(rs.getString("path"));
        dto.setLikeExists(rs.getInt("is_liked"));
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dto.setModifiedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return dto;
    };


    private final RowMapper<SellPostDTO.SellPostResponseDto> sellPostWithMemberRowMapper = (rs, rowNum) -> {
        SellPostDTO.SellPostResponseDto dto=new SellPostDTO.SellPostResponseDto();
        dto.setSellPostId(rs.getLong("sell_post_id"));
        dto.setTitle(rs.getString("title"));
        dto.setDescription(rs.getString("description"));
        dto.setPrice(rs.getInt("price"));
        dto.setCity(rs.getString("city"));
        dto.setMemberId(rs.getLong("member_id"));
        dto.setLikeCount(rs.getInt("like_count"));
        dto.setCommentCount(rs.getInt("comment_count"));
        dto.setGu(rs.getString("gu"));
        dto.setStatus(rs.getInt("status"));
        dto.setDong(rs.getString("dong"));
        dto.setLikeExists(rs.getInt("is_liked"));
        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dto.setModifiedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return dto;
    };

}

