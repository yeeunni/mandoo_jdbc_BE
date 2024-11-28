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
    private final MemberRepository memberRepository;
    private final SellPostCategoryRepository sellPostCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SellImagePathRepository sellImagePathRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public SellPostDTO.SellPostResponseDto SellPostcreate(SellPostDTO.SellPostCreateDto request) {

        if (request.getMemberId() == null) {
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND));

        SellPost sellPost = sellPostConverter.sellPostCreateDto(request, member);

        List<SellPostCategory> sellPostCategories = request.getCategoryIds().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND));
                    return SellPostCategory.builder()
                            .sellPost(sellPost)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());
        // SellPostì ì¹´íê³ ë¦¬ ì¤ì 
        sellPost.setCategories(sellPostCategories);

        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());

        if (!images.isEmpty()) {
            List<SellImagePath> imagePaths = images.stream().map(file -> {
                try {
                    // íì¼ ì ì¥
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadPath, fileName).toString();

                    File directory = new File(uploadPath);
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new RuntimeException("Directory creation failed: " + uploadPath);
                    }
                    file.transferTo(new File(filePath));

                    // SellImagePath ìì±
                    return SellImagePath.builder()
                            .path(filePath)
                            .sellPost(sellPost) // ì°ê´ ì¤ì 
                            .build();

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());


            // ì´ë¯¸ì§ ì ì¥
            sellPost.setImages(imagePaths); // SellPostì ì°ê²°
        }

        // JDBC를 통한 데이터 저장
        String sql = "INSERT INTO sellpost (title, price, description, city, gu, dong, created_at, modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                sellPost.getTitle(),
                sellPost.getPrice(),
                sellPost.getDescription(),
                sellPost.getCity(),
                sellPost.getGu(),
                sellPost.getDong(),
                sellPost.getCreatedAt(),
                sellPost.getModifiedAt()
        );


        // ìëµ DTO ìì±
        SellPostDTO.SellPostResponseDto responseDto = SellPostConverter.sellPostResponseDto(sellPost);

        return responseDto;

    }

    @Override
    public SellPostDTO.SellPostResponseDto getSellPostById(Long id) {
        // ID로 게시물 조회
        String sql = "SELECT * FROM sellpost WHERE sell_post_id = ?";
        List<SellPost> results = jdbcTemplate.query(sql, sellPostRowMapper, id);

        // 결과 확인
        if (results.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }

        SellPost sellPost = results.get(0);

        // 엔티티를 DTO로 변환 후 반환
        return SellPostConverter.sellPostResponseDto(sellPost);
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
        sellPost.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return sellPost;
    };

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = null;
//        category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setName(rs.getString("name"));
        return category;
    };


    @Override
    public SellPostDTO.SellPostResponseDto updateSellPost(Long sellPostId, SellPostDTO.SellPostUpdateDto request) {
        // 게시물 조회 쿼리
        String findPostSql = "SELECT * FROM sellpost WHERE sell_post_id = ?";
        List<SellPost> results = jdbcTemplate.query(findPostSql, sellPostRowMapper, sellPostId);

        if (results.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }

        SellPost sellPost = results.get(0);

        // 카테고리 처리
        List<Long> categoryIds = Optional.ofNullable(request.getCategoryIds()).orElse(Collections.emptyList());
        List<SellPostCategory> categories = categoryIds.stream()
                .map(categoryId -> {
                    String findCategorySql = "SELECT * FROM category WHERE category_id = ?";
                    List<Category> categoryResults = jdbcTemplate.query(findCategorySql, categoryRowMapper, categoryId);

                    if (categoryResults.isEmpty()) {
                        throw new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND);
                    }

                    return SellPostCategory.builder()
                            .sellPost(sellPost)
                            .category(categoryResults.get(0))
                            .build();
                }).collect(Collectors.toList());

        sellPost.setCategories(categories);

        // 이미지 처리
        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());
        if (!images.isEmpty()) {
            // 기존 이미지 삭제
            String deleteImageSql = "DELETE FROM sellimagepath WHERE sellpost_id = ?";
            jdbcTemplate.update(deleteImageSql, sellPostId);

            // 새 이미지 추가
            List<SellImagePath> imagePaths = images.stream().map(file -> {
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

                    return SellImagePath.builder()
                            .path(filePath)
                            .sellPost(sellPost)
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());

            sellPost.setImages(imagePaths);
        }

        // 필드 업데이트
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

        // 업데이트된 데이터를 DTO로 변환 후 반환
        return SellPostConverter.sellPostResponseDto(sellPost);
    }



    @Override
    public void deleteSellPost(Long id) {
        if (!sellPostRepository.existsById(id)) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }
        sellPostRepository.deleteById(id);
    }

    @Override
    public Page<SellPostDTO.SellPostResponseDto> getRecentSellPosts (Pageable pageable) {//ëª¨ë  ê²ìë¬¼ ìµì ìì¼ë¡ ì¡°í
       Page<SellPost> sellPostPage = sellPostRepository.findAllByOrderByCreatedAtDesc(pageable);
       return sellPostPage.map(SellPostConverter::sellPostGetResponse);
    }
}

