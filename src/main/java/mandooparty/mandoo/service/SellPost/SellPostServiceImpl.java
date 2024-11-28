package mandooparty.mandoo.service.SellPost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mandooparty.mandoo.converter.SellPostConverter;
import mandooparty.mandoo.domain.*;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.*;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellPostServiceImpl implements SellPostService {

    private final SellPostRepository sellPostRepository;
    private final SellPostConverter sellPostConverter;
    private final MemberRepository memberRepository;
    private final SellPostCategoryRepository sellPostCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SellImagePathRepository sellImagePathRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    @Transactional
    public SellPostDTO.SellPostResponseDto SellPostcreate(SellPostDTO.SellPostCreateDto request) {
        log.info("Received memberId: {}", request.getMemberId());

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
        // SellPost에 카테고리 설정
        sellPost.setCategories(sellPostCategories);

        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());

        if (!images.isEmpty()) {
            List<SellImagePath> imagePaths = images.stream().map(file -> {
                try {
                    // 파일 저장
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadPath, fileName).toString();

                    File directory = new File(uploadPath);
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new RuntimeException("Directory creation failed: " + uploadPath);
                    }
                    file.transferTo(new File(filePath));

                    // SellImagePath 생성
                    return SellImagePath.builder()
                            .path(filePath)
                            .sellPost(sellPost) // 연관 설정
                            .build();

                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }).collect(Collectors.toList());


            // 이미지 저장
            sellPost.setImages(imagePaths); // SellPost와 연결

        }


        sellPostRepository.save(sellPost);
        sellPostRepository.flush();

        // 응답 DTO 생성
        SellPostDTO.SellPostResponseDto responseDto = SellPostConverter.sellPostResponseDto(sellPost);



        return responseDto;

    }

    @Override
    @Transactional(readOnly = true)
    public SellPostDTO.SellPostResponseDto getSellPostById(Long id) {
        // ID로 게시물 조회
        SellPost sellPost = sellPostRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.POST_NOT_FOUND));

        // 엔티티를 DTO로 변환 후 반환
        return SellPostConverter.sellPostResponseDto(sellPost);
    }

    @Override
    @Transactional
    public SellPostDTO.SellPostResponseDto updateSellPost(Long sellPostId, SellPostDTO.SellPostUpdateDto request) {

        SellPost sellPost = sellPostRepository.findById(sellPostId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.POST_NOT_FOUND));

//        if (!Objects.equals(request.getMemberId(), sellPost.getMember().getId())) {
//            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_AUTHORIZED);
//        }

//        List<SellPostCategory> categories = request.getCategoryIds().stream()
//                .map(categoryId -> sellPostCategoryRepository.findById(categoryId)
//                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND)))
//                .collect(Collectors.toList());

        // 3. 카테고리 처리 (Null 방어)
        List<Long> categoryIds = Optional.ofNullable(request.getCategoryIds()).orElse(Collections.emptyList());
        List<SellPostCategory> categories = categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND)))
                .map(category -> SellPostCategory.builder()
                        .sellPost(sellPost)
                        .category(category)
                        .build())
                .collect(Collectors.toList());
        sellPost.setCategories(categories);


        // 4. 이미지 처리
        List<MultipartFile> images = Optional.ofNullable(request.getImages()).orElse(Collections.emptyList());


        // 기존 이미지 삭제 및 새 이미지 추가
        if (!images.isEmpty()) {
            if (sellPost.getImages() != null) {
                sellPost.getImages().clear();
            }

            List<SellImagePath> imagePaths = images.stream().map(file -> {
                try {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    String filePath = Paths.get(uploadPath, fileName).toString();

                    File directory = new File(uploadPath);
                    if (!directory.exists() && !directory.mkdirs()) {
                        throw new RuntimeException("Directory creation failed: " + uploadPath);
                    }
                    file.transferTo(new File(filePath));


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
        int price = Optional.ofNullable(request.getPrice()).orElse(0);
        sellPost.update(
                request.getTitle(),
                price,
                request.getDescription(),
                request.getCity(),
                request.getGu(),
                request.getDong(),
                categories
        );

        sellPostRepository.save(sellPost);

        // 5. 업데이트된 엔티티를 DTO로 변환하여 반환
        return SellPostConverter.sellPostResponseDto(sellPost);

    }

    @Override
    @Transactional
    public void deleteSellPost(Long id) {
        if (!sellPostRepository.existsById(id)) {
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }
        sellPostRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Page<SellPostDTO.SellPostResponseDto> getRecentSellPosts (Pageable pageable) {//모든 게시물 최신순으로 조회
       Page<SellPost> sellPostPage = sellPostRepository.findAllByOrderByCreatedAtDesc(pageable);
       return sellPostPage.map(SellPostConverter::sellPostGetResponse);
    }
}

