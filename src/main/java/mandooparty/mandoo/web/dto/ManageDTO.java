package mandooparty.mandoo.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ManageDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManageDashBoardSellPostDto{
        private LocalDate date;
        private Integer sellPostCount;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManageDashBoardCategoryRatioDto{
        private String name;
        private Integer ratio;
        private Integer categoryCount;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManageDashBoardDateViewDto{
        private LocalDate date;
        private Integer subscriber;
        private Integer sellPost;
        private Integer comment;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManageDashBoardDto{
        private List<ManageDashBoardSellPostDto> sellPostCount;
        private List<ManageDashBoardCategoryRatioDto> categoryRatio;

        private List<ManageDashBoardDateViewDto> dateView;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManageMemberDto{
        private Long memberId;
        private String passWord;
        private String email;
        private Integer status;
        private String name;
        private String nickName;
        private Integer writeSellPostCount;
        private Integer likeSellPostCount;
        private Integer completedSellPostCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;


    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentReportDto{
        private Long commentReportId;
        private Long commentId;
        private String content;
        private Integer status;
        private Long memberId;
        private Long writerId;
        private Long sellPostId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Integer commentReportCount;


    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostReportDto{
        private Long postReportId;
        private Long sellPostId;
        private String title;
        private String content;
        private Long writerId;
        private Long memberId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Integer postReportCount;


    }

}
