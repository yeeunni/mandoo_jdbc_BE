package mandooparty.mandoo.web.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mandooparty.mandoo.domain.SellPost;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;


@Data
public class SellPostDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostWritePageDto {
        private List<String> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostUpdatePageDto {
        private List<String> categories;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostCreateDto {

        private String title;
        private Integer price;
        private String description;
        private String city;
        private String gu;
        private String dong;
        private List<Long> categoryIds;
        private Long memberId;

        private List<MultipartFile> images = new ArrayList<>(); // 기본값 설정
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostResponseDto {
        private Long sellPostId;
        private String title;
        private Integer price;
        private String description;
        private String city;
        private String gu;
        private String dong;
        private Long memberId;
        private Integer likeCount;
        private Integer commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private List<String> categories;
        private List<String> images;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostUpdateDto {
        private String title;
        private Integer price;
        private String description;
        private String city;
        private String gu;
        private String dong;
        private Long memberId;
        private List<Long> categoryIds;
        private List<MultipartFile> images;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostDeleteDto {
        private Long sellPostId;
    }

}
