package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.enums.MemberStatus;
import mandooparty.mandoo.web.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertMember(Member member)
    {
        String email = member.getEmail();
        String password = member.getPassword();
        String nickname = member.getNickname();
        MemberStatus status = member.getStatus();
        String sql = "INSERT INTO Member (email, password, nickname, status) " +
                "VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, email, password, nickname, status);
        return rowsAffected > 0;
    }
    public void deleteUserById(Long id) {
        // DELETE
        String sql = "DELETE FROM Member WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
    public Optional<Member> findById (Long id) {
        String sql = "SELECT * FROM Member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    new BeanPropertyRowMapper<>(Member.class)
            );
            return Optional.ofNullable(member);  // member가 null이면 Optional.empty() 반환
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // 결과가 없으면 Optional.empty() 반환
        }
    }
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


    Long getCountByDate(LocalDate day)
    {
        String sql="SELECT COUNT(*) FROM member WHERE DATE(created_at)=?";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            return jdbcTemplate.queryForObject(sql, new Object[]{sqlDate},Long.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    List<Member> findByLoginTime(LocalDate day)
    {
        String sql="SELECT * FROM Member WHERE DATE(login_time) < ? ";
        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(day);
            // parentComment의 ID를 추출하여 쿼리 실행
            return jdbcTemplate.query(sql, new Object[]{sqlDate},
                    new BeanPropertyRowMapper<>(Member.class));
        } catch (EmptyResultDataAccessException e) {
            return null; // 결과가 없으면 null 반환
        }

    }
}