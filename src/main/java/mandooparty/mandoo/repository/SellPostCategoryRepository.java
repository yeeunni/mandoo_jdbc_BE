package mandooparty.mandoo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SellPostCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> countCategory() {
        String sql = "SELECT c.name AS categoryName, " +
                "COUNT(*) AS count, " +
                "(COUNT(*) * 1.0 / (SELECT COUNT(*) FROM sell_post_category)) * 100 AS percentage " +
                "FROM sell_post_category sp " +
                "JOIN category c ON sp.category_id = c.id " +
                "GROUP BY c.id, c.name";

        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<>();
                result.put("categoryName", rs.getString("categoryName")); // String
                result.put("count", rs.getInt("count"));                 // Integer
                result.put("percentage", rs.getDouble("percentage"));    // Double
                return result;
            }
        });
    }

}
