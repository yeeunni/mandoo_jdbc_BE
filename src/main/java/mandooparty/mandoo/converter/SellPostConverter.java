package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.SellPost;
import mandooparty.mandoo.domain.Category;
import mandooparty.mandoo.domain.SellPostCategory;
import mandooparty.mandoo.web.dto.CommentDTO;
import mandooparty.mandoo.domain.SellImagePath;
import mandooparty.mandoo.repository.CategoryRepository;
import mandooparty.mandoo.repository.CommentRepository;
import mandooparty.mandoo.repository.SellImagePathRepository;
import mandooparty.mandoo.web.dto.SellPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SellPostConverter {

    // SellPostCreateDto -> SellPost 엔티티로 변환
    public SellPost sellPostCreateDto(SellPostDTO.SellPostCreateDto dto, Member member) {
        List<SellImagePath> imagePaths = dto.getImages().stream()
                .map(file -> SellImagePath.builder()
                        .path(file.getOriginalFilename()) // 실제 파일 저장 후 경로 설정이 필요함
                        .build())
                .collect(Collectors.toList());

        return SellPost.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .city(dto.getCity())
                .gu(dto.getGu())
                .dong(dto.getDong())
                .member_id(member.getId())
                .created_at(LocalDateTime.now())     // 생성일자 설정
                .updated_at(LocalDateTime.now())   // 수정일자 설정
                .build();
    }

    private static CategoryRepository categoryRepository;
    private static SellImagePathRepository sellImagePathRepository;
    private static CommentRepository commentRepository;

    @Autowired
    public SellPostConverter(CategoryRepository categoryRepository, SellImagePathRepository sellImagePathRepository, CommentRepository commentRepository) {
        this.categoryRepository = categoryRepository;
        this.sellImagePathRepository = sellImagePathRepository;
        this.commentRepository =  commentRepository;
    }

    // SellPost -> SellPostResponseDto 변환
    public static SellPostDTO.SellPostResponseDto sellPostResponseDto(SellPost sellPost) {

        List<String> categories = categoryRepository.findCategoryNamesBySellPostId(sellPost.getSell_post_id());
        List<String> images = sellImagePathRepository.findImagePathsBySellPostId(sellPost.getSell_post_id());
        // 댓글과 작성자 닉네임 조회
        List<CommentDTO.CommentResponseDto> comments = commentRepository.findCommentsBySellPostId(sellPost.getSell_post_id());

        return SellPostDTO.SellPostResponseDto.builder()
                .sellPostId(sellPost.getSell_post_id())   // 게시글 ID 설정
                .title(sellPost.getTitle())             // 게시글 제목 설정
                .price(sellPost.getPrice())             // 가격 설정
                .description(sellPost.getDescription()) // 게시글 설명 설정
                .city(sellPost.getCity())               // 게시물 위치 - 시 설정
                .gu(sellPost.getGu())                   // 게시물 위치 - 구 설정
                .dong(sellPost.getDong())               // 게시물 위치 - 동 설정
                .likeCount(sellPost.getLike_count())     // 좋아요 수 설정
                .commentCount(sellPost.getComment_count()) // 댓글 수 설정
                .createdAt(sellPost.getCreated_at())     // 생성일자 설정
                .modifiedAt(sellPost.getUpdated_at())   // 수정일자 설정
                .memberId(sellPost.getMember_id())
                .categories(categories)                 // 카테고리 설정
                .images(images)                         // 이미지 경로 설정
                .comments(comments)
                .build();
    }


    public static SellPostDTO.SellPostResponseDto sellPostGetResponse(SellPost sellPost) {
        return SellPostDTO.SellPostResponseDto.builder()
                .sellPostId(sellPost.getSell_post_id())   // 게시글 ID 설정
                .title(sellPost.getTitle())             // 게시글 제목 설정
                .price(sellPost.getPrice())             // 가격 설정
                .description(sellPost.getDescription()) // 게시글 설명 설정
                .city(sellPost.getCity())               // 게시물 위치 - 시 설정
                .gu(sellPost.getGu())                   // 게시물 위치 - 구 설정
                .dong(sellPost.getDong())               // 게시물 위치 - 동 설정
                .likeCount(sellPost.getLike_count())     // 좋아요 수 설정
                .commentCount(sellPost.getComment_count()) // 댓글 수 설정
                .createdAt(sellPost.getCreated_at())     // 생성일자 설정
                .modifiedAt(sellPost.getUpdated_at())   // 수정일자 설정
                .memberId(sellPost.getMember_id() != null ? sellPost.getMember_id() : null) // memberId 설정
                .build();
    }
}