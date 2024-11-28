package mandooparty.mandoo.service.SellPost;

import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.converter.SellPostConverter;
import mandooparty.mandoo.domain.*;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.*;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellPostServiceImpl implements SellPostService {

    private final SellPostRepository sellPostRepository;
    private final SellPostConverter sellPostConverter;
    private final JdbcTemplate jdbcTemplate;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public SellPostDTO.SellPostResponseDto SellPostcreate(SellPostDTO.SellPostCreateDto request) {
        if (request.getMemberId() == null) {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }

        // 멤버 조회
        String findMemberSql = "SELECT * FROM member WHERE id = ?";
        Member member = jdbcTemplate.queryForObject(findMemberSql, new Object[]{request.getMemberId()}, memberRowMapper);

        if (member == null) {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }

        // SellPost 생성
        SellPost sellPost = sellPostConverter.sellPostCreateDto(request, member);

        // SellPost 데이터베이스 삽입
        String insertPostSql = "INSERT INTO sellpost (title, price, description, city, gu, dong, member_id, created_at, modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertPostSql,
                sellPost.getTitle(),
                sellPost.getPrice(),
                sellPost.getDescription(),
                sellPost.getCity(),
                sellPost.getGu(),
                sellPost.getDong(),
                member.getId(),
                sellPost.getCreatedAt(),
                sellPost.getModifiedAt()
        );

        // 카테고리 처리
        List<Long> categoryIds = Optional.ofNullable(request.getCategoryIds()).orElse(Collections.emptyList());
        for (Long categoryId : categoryIds) {
            String findCategorySql = "SELECT * FROM category WHERE category_id = ?";
            Category category = jdbcTemplate.queryForObject(findCategorySql, new Object[]{categoryId}, categoryRowMapper);

            if (category == null) {
                throw new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND);
            }

            String insertCategorySql = "INSERT INTO sellpostCategory (sellpost_id, category_id) VALUES (?, ?)";
            jdbcTemplate.update(insertCategorySql, sellPost.getSellPostId(), categoryId);
        }

        // 이미지 처리
        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());
        if (!images.isEmpty()) {
            for (MultipartFile file : images) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadPath, fileName).toString();

                    File directory = new File(uploadPath);
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new RuntimeException("Directory creation failed: " + uploadPath);
                    }
                    file.transferTo(new File(filePath));

                    String insertImageSql = "INSERT INTO sellimagepath (path, sellpost_id) VALUES (?, ?)";
                    jdbcTemplate.update(insertImageSql, filePath, sellPost.getSellPostId());

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }
        }

        return SellPostConverter.sellPostResponseDto(sellPost);
    }

    @Override
    public SellPostDTO.SellPostResponseDto getSellPostById(Long id) {
        String sql = "SELECT * FROM sellpost WHERE sell_post_id = ?";
        SellPost sellPost = jdbcTemplate.queryForObject(sql, new Object[]{id}, sellPostRowMapper);

        if (sellPost == null) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }

        return SellPostConverter.sellPostResponseDto(sellPost);
    }

    @Override
    public SellPostDTO.SellPostResponseDto updateSellPost(Long sellPostId, SellPostDTO.SellPostUpdateDto request) {
        String findPostSql = "SELECT * FROM sellpost WHERE sell_post_id = ?";
        SellPost sellPost = jdbcTemplate.queryForObject(findPostSql, new Object[]{sellPostId}, sellPostRowMapper);

        if (sellPost == null) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }

        // 카테고리 처리
        List<Long> categoryIds = Optional.ofNullable(request.getCategoryIds()).orElse(Collections.emptyList());
        String deleteCategorySql = "DELETE FROM sellpostCategory WHERE sellpost_id = ?";
        jdbcTemplate.update(deleteCategorySql, sellPostId);

        for (Long categoryId : categoryIds) {
            String findCategorySql = "SELECT * FROM category WHERE category_id = ?";
            Category category = jdbcTemplate.queryForObject(findCategorySql, new Object[]{categoryId}, categoryRowMapper);

            if (category == null) {
                throw new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND);
            }

            String insertCategorySql = "INSERT INTO sellpostCategory (sellpost_id, category_id) VALUES (?, ?)";
            jdbcTemplate.update(insertCategorySql, sellPostId, categoryId);
        }

        // 이미지 처리
        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());
        String deleteImageSql = "DELETE FROM sellimagepath WHERE sellpost_id = ?";
        jdbcTemplate.update(deleteImageSql, sellPostId);

        if (!images.isEmpty()) {
            for (MultipartFile file : images) {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadPath, fileName).toString();

                    File directory = new File(uploadPath);
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new RuntimeException("Directory creation failed: " + uploadPath);
                    }
                    file.transferTo(new File(filePath));

                    String insertImageSql = "INSERT INTO sellimagepath (path, sellpost_id) VALUES (?, ?)";
                    jdbcTemplate.update(insertImageSql, filePath, sellPostId);

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }
        }

        // 게시물 업데이트
        String updatePostSql = "UPDATE sellpost SET title = ?, price = ?, description = ?, city = ?, gu = ?, dong = ? WHERE sell_post_id = ?";
        jdbcTemplate.update(updatePostSql,
                request.getTitle(),
                Optional.ofNullable(request.getPrice()).orElse(0),
                request.getDescription(),
                request.getCity(),
                request.getGu(),
                request.getDong(),
                sellPostId
        );

        return SellPostConverter.sellPostResponseDto(sellPost);
    }

    @Override
    public void deleteSellPost(Long id) {
        String sql = "DELETE FROM sellpost WHERE sell_post_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }
    }

    @Override
    public Page<SellPostDTO.SellPostResponseDto> getRecentSellPosts(Pageable pageable) {
        String sql = "SELECT * FROM sellpost ORDER BY created_at DESC LIMIT ? OFFSET ?";
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        List<SellPost> sellPosts = jdbcTemplate.query(sql, new Object[]{limit, offset}, sellPostRowMapper);

        String countSql = "SELECT COUNT(*) FROM sellpost";
        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class);

        return new PageImpl<>(
                sellPosts.stream()
                        .map(SellPostConverter::sellPostResponseDto)
                        .collect(Collectors.toList()),
                pageable,
                totalElements
        );
    }

    private final RowMapper<SellPost> sellPostRowMapper = (rs, rowNum) -> {
        SellPost sellPost = new SellPost();
        sellPost.setId(rs.getLong("sell_post_id"));
        sellPost.setTitle(rs.getString("title"));
        sellPost.setDescription(rs.getString("description"));
        sellPost.setPrice(rs.getInt("price"));
        sellPost.setCity(rs.getString("city"));
        sellPost.setGu(rs.getString("gu"));
        sellPost.setDong(rs.getString("dong"));
        sellPost.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        sellPost.setModifiedAt(rs.getTimestamp("modified_at").toLocalDateTime());
        return sellPost;
    };

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getLong("member_id"));
        member.setEmail(rs.getString("email"));
        // 기타 필드 매핑
        return member;
    };

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setName(rs.getString("name"));
        return category;
    };
}
