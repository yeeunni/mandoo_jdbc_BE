package mandooparty.mandoo.converter;

import mandooparty.mandoo.domain.*;
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
                .writeSellPostCount(member.getWrite_sell_post_count())
                .likeSellPostCount(member.getLike_sell_post_count())
                .completedSellPostCount(member.getCompleted_sell_post_count())
                .createdAt(member.getCreated_at())
                .modifiedAt(member.getUpdated_at())
                .build();
    }

    public static ManageDTO.CommentReportDto ManageCommentReportDto(CommentReport commentReport, Comment comment){
        return ManageDTO.CommentReportDto.builder()
                .commentReportId(commentReport.getId())
                .commentId(commentReport.getComment_id())
                .memberId(commentReport.getMember_id())
                .content(comment.getContent())
                .writerId(comment.getMember_id())
                .sellPostId(comment.getSell_post_id())
                .commentReportCount(commentReport.getComment_report_count())
                .createdAt(commentReport.getCreated_at())
                .modifiedAt(commentReport.getUpdated_at())
                .build();
    }

    public static ManageDTO.PostReportDto ManagePostReportDto(PostReport postReport, SellPost sellPost){
        return ManageDTO.PostReportDto.builder()
                .postReportId(postReport.getId())
                .sellPostId(postReport.getSell_post_id())
                .title(sellPost.getTitle())
                .content(sellPost.getDescription())
                .writerId(sellPost.getMember_id())
                .memberId(postReport.getMember_id())
                .createdAt(postReport.getCreated_at())
                .memberId(postReport.getMember_id())
                .createdAt(postReport.getUpdated_at())
                .modifiedAt(postReport.getUpdated_at())
                .postReportCount(postReport.getPost_report_count())
                .build();
    }

}
