package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.SellImagePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SellImagePathRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> findImagePathsBySellPostId(Long sellPostId) {
        String sql = "SELECT path FROM sellimagepath WHERE sell_post_id = ?";
        return jdbcTemplate.query(sql, new Object[]{sellPostId}, (rs, rowNum) -> rs.getString("path"));
    }
}