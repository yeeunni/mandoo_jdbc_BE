package mandooparty.mandoo.service.SellPost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.domain.Category;
import mandooparty.mandoo.repository.CategoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAllCategory() {
        // SQL을 직접 작성하여 카테고리 이름 가져오기
        String sql = "SELECT name FROM category";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
    }
}

