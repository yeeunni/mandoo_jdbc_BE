package mandooparty.mandoo.repository;


import mandooparty.mandoo.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository  {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Category> findAll()
    {
        String sql = "SELECT * FROM category";

        // Execute the query and map results to Category objects
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    public List<String> findCategoryNamesBySellPostId(Long sellPostId) {
        String sql = "SELECT c.name FROM category c " +
                "JOIN sellpostcategory sc ON c.category_id = sc.category_id " +
                "WHERE sc.sell_post_id = ?";
        return jdbcTemplate.query(sql, new Object[]{sellPostId}, (rs, rowNum) -> rs.getString("name"));
    }

    public boolean insertSellPostCategory(Long sellPostId, Long categoryId) {
        String sql = "INSERT INTO sellpostCategory (sellpost_id, category_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, sellPostId, categoryId);
        return rowsAffected > 0;
    }


}
