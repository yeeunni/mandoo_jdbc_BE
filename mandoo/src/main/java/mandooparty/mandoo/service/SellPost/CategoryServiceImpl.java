package mandooparty.mandoo.service.SellPost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAllCategory() {
        // SQL 쿼리를 사용하여 모든 카테고리 이름을 조회
        String sql = "SELECT name FROM category";

        // 데이터베이스에서 카테고리 이름 리스트를 조회
        return jdbcTemplate.queryForList(sql, String.class);
    }
}

