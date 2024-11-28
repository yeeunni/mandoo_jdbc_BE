package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.domain.PostReport;
import mandooparty.mandoo.web.dto.ManageDTO;
import org.springframework.stereotype.Component;

@Component
public class ManageConverter {

    public static ManageDTO.ManageMemberDto ManageMemberDto(Member member){
        return ManageDTO.ManageMemberDto.builder()
                .memberId(member.getId())
                .passWord(member.getPassword())
                .email(member.getEmail())
                .status(member.getStatus())
                .nickName(member.getNickname())
                .writeSellPostCount(member.getWriteSellPostCount())
                .likeSellPostCount(member.getLikeSellPostCount())
                .completedSellPostCount(member.getCompletedSellPostCount())
                .createdAt(member.getCreated_at())
                .modifiedAt(member.getUpdated_at())
                .build();
    }

    public static ManageDTO.CommentReportDto ManageCommentReportDto(CommentReport commentReport){
        return ManageDTO.CommentReportDto.builder()
                .commentReportId(commentReport.getId())
                .commentId(commentReport.getComment().getId())
                .memberId(commentReport.getMember().getId())
                .content(commentReport.getComment().getContent())
                .writerId(commentReport.getComment().getMember().getId())
                .sellPostId(commentReport.getComment().getSellPost().getSellPostId())
                .commentReportCount(commentReport.getCommentReportCount())
                .createdAt(commentReport.getCreatedAt())
                .modifiedAt(commentReport.getUpdateAt())
                .build();
    }

    public static ManageDTO.PostReportDto ManagePostReportDto(PostReport postReport){
        return ManageDTO.PostReportDto.builder()
                .postReportId(postReport.getId())
                .sellPostId(postReport.getSellPost().getSellPostId())
                .title(postReport.getSellPost().getTitle())
                .content(postReport.getSellPost().getDescription())
                .writerId(postReport.getSellPost().getMember().getId())
                .memberId(postReport.getMember().getId())
                .createdAt(postReport.getCreatedAt())
                .memberId(postReport.getMember().getId())
                .createdAt(postReport.getUpdateAt())
                .modifiedAt(postReport.getUpdateAt())
                .postReportCount(postReport.getPostReportCount())
                .build();
    }

}
