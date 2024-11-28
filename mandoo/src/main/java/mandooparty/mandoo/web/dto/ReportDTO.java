package mandooparty.mandoo.web.dto;

import lombok.*;
import mandooparty.mandoo.domain.enums.CommentStatus;

@Data
public class ReportDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellPostReportResponseDto{
        private Long sellPostReportId;
        private Long memberId;
        private Long sellPostId;
        private Integer postReportCount;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentReportResponseDto{
        private Long commentReportId;
        private Long memberId;
        private Long commentId;
        private Integer commentReportCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberIdRequestDto{//myPage request
        private Long memberId;
    }
}
