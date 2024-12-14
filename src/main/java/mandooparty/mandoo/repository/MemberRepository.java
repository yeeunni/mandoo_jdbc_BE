package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Member> findById(Long memberId)
    {
        String sql="SELECT m.* FROM member AS m WHERE m.id=?";
        try {
            Member member= jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{memberId},
                    new BeanPropertyRowMapper<>(Member.class)  // 수정된 부분
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }
    public Member insertMember(Member member) {
        String email = member.getEmail();
        String password = member.getPassword();
        String nickname = member.getNickname();
        Integer status = member.getStatus();
        LocalDateTime createdAt = member.getCreated_at();
        LocalDateTime updatedAt = member.getUpdated_at();

        String sql = "INSERT INTO member (email, password, nickname, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // KeyHolder를 사용하여 자동 생성된 ID 값을 반환
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, nickname);
            ps.setInt(4, status);
            ps.setObject(5, createdAt);
            ps.setObject(6, updatedAt);
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            // 자동 생성된 ID 값을 얻어 member에 설정
            Long generatedId = keyHolder.getKey().longValue();
            member.setId(generatedId);  // assuming `setId()` method exists in your `Member` class
            return member;
        } else {
            return null;  // 삽입 실패 시 null 반환
        }
    }

    public boolean updateLoginStatusByEmail(String email, int isLogin) {
        String sql = "UPDATE member SET is_login = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(sql, isLogin, email);
        return rowsAffected > 0; // 업데이트된 행이 있으면 true, 없으면 false 반환
    }
    // 로그인 시간을 업데이트하는 메서드
    public boolean updateLoginTimeByEmail(String email, LocalDateTime loginTime) {
        String sql = "UPDATE member SET login_time = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(sql, loginTime, email); // is_login도 함께 업데이트
        return rowsAffected > 0; // 업데이트된 행이 있으면 true, 없으면 false 반환
    }


    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM Member WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{email},
                    new BeanPropertyRowMapper<>(Member.class)
            );
            return Optional.ofNullable(member);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }
//    public boolean updateLoginStatusByEmail(String email, int isLogin) {
//        String sql = "UPDATE Member SET is_login = ? WHERE email = ?";
//        int rowsAffected = jdbcTemplate.update(sql, isLogin, email);
//        return rowsAffected > 0; // 업데이트된 행이 있으면 true, 없으면 false 반환
//    }

    public Optional<Member> findByNickname(String nickname) {
        String sql = "SELECT * FROM Member WHERE nickname = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{nickname},
                    new BeanPropertyRowMapper<>(Member.class)
            );
            return Optional.ofNullable(member);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }


    public Long getCountByDate(LocalDate day)
    {
        String sql="SELECT COUNT(*) FROM member WHERE DATE(created_at)=?";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.queryForObject(sql, new Object[]{sqlDate},Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Member> findByLoginTime(LocalDate day, String order) {
        // 기본 SQL 쿼리
        String sql = "SELECT * FROM Member WHERE DATE(login_time) < ?";

        // order가 비어있지 않으면 ORDER BY 추가
        if (order != null && !order.trim().isEmpty()) {
            sql += " ORDER BY " + order; // SQL Injection 위험 방지를 위해 직접 매개변수로 사용하지 않음
        }

        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.query(sql,
                    new Object[]{sqlDate},
                    new BeanPropertyRowMapper<>(Member.class));
        } catch (EmptyResultDataAccessException e) {
            return List.of(); // 결과가 없으면 빈 리스트 반환
        }
    }



    public boolean deleteById(Long memberId) {
        String sql = "DELETE FROM member WHERE id = ?"; // 테이블 및 열 이름을 스키마에 맞게 수정
        int rowsAffected = jdbcTemplate.update(sql, new Object[]{memberId});
        return rowsAffected > 0; // 영향을 받은 행이 있으면 true, 없으면 false 반환
    }
}