package mandooparty.mandoo.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner{
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // 트리거 존재 여부 확인
        String checkTrigger = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA = ? AND TRIGGER_NAME = ?";
        Integer countComment = jdbcTemplate.queryForObject(checkTrigger, new Object[]{"mandoo_dev", "increase_comment_count"}, Integer.class);

        // 트리거가 존재하지 않으면 생성
        if (countComment == null || countComment == 0) {
            String createTrigger =" CREATE TRIGGER `increase_comment_count`" +
                    "    AFTER INSERT ON `Comment`" +
                    "    FOR EACH ROW" +
                    "    BEGIN" +
                    "        UPDATE `SellPost`" +
                    "        SET `comment_count` = `comment_count` + 1" +
                    "        WHERE `sell_post_id` = NEW.`sell_post_id`;" +
                    "    END;";
            jdbcTemplate.execute(createTrigger);
        }

        Integer countLike=jdbcTemplate.queryForObject(checkTrigger,new Object[]{"mandoo_dev","increase_like_count"},Integer.class);
        if(countLike==null|| countLike==0){
            String createTrigger =" CREATE TRIGGER `increase_like_count`" +
                    "    AFTER INSERT ON `Likes`" +
                    "    FOR EACH ROW" +
                    "    BEGIN" +
                    "        UPDATE `SellPost`" +
                    "        SET `like_count` = `like_count` + 1" +
                    "        WHERE `sell_post_id` = NEW.`sell_post_id`;" +
                    "    END;";
            jdbcTemplate.execute(createTrigger);
        }


        Integer countSellPostCount =jdbcTemplate.queryForObject(checkTrigger,new Object[]{"mandoo_dev","increase_write_sell_post_count"},Integer.class);
        if(countSellPostCount==null|| countSellPostCount==0){
            String createTrigger = "CREATE TRIGGER `increase_write_sell_post_count` " +
                    "AFTER INSERT ON `SellPost` " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "    UPDATE `Member` " +
                    "    SET `write_sell_post_count` = `write_sell_post_count` + 1 " +
                    "    WHERE `id` = NEW.`member_id`; " +
                    "END; " ;
            jdbcTemplate.execute(createTrigger);
        }

        Integer countCompledtedSellPostCount =jdbcTemplate.queryForObject(checkTrigger,new Object[]{"mandoo_dev","increase_completed_sell_post_count"},Integer.class);
        if(countCompledtedSellPostCount==null|| countCompledtedSellPostCount==0){
            String createTrigger = "CREATE TRIGGER `increase_completed_sell_post_count` " +
                    "AFTER UPDATE ON `SellPost` " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "    IF NEW.`status` = 1 AND OLD.`status` != 1 THEN " +
                    "        UPDATE `Member` " +
                    "        SET `completed_sell_post_count` = `completed_sell_post_count` + 1 " +
                    "        WHERE `id` = NEW.`member_id`; " +
                    "    END IF; " +
                    "END";

            jdbcTemplate.execute(createTrigger);

        }


        String checkIndex = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND INDEX_NAME = ?";
        Integer countIndex = jdbcTemplate.queryForObject(checkIndex, new Object[]{"mandoo_dev", "sellpost", "idx_title"}, Integer.class);

        if (countIndex == null || countIndex == 0) {
            String createIndex = "CREATE FULLTEXT INDEX idx_title ON sellpost (title) WITH PARSER ngram;";
            jdbcTemplate.execute(createIndex);
        }
    }


}
