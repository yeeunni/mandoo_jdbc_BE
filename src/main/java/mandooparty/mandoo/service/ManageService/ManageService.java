package mandooparty.mandoo.service.ManageService;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.Member;
import mandooparty.mandoo.web.dto.ManageDTO;

import java.util.List;

public interface ManageService {

    public List<ManageDTO.ManageDashBoardSellPostDto> getDaySellPostCount();

    public List<ManageDTO.ManageDashBoardCategoryRatioDto> getCategoryRatio();

    public List<ManageDTO.ManageDashBoardDateViewDto> getDateView();

    public List<Member> getMember();

    public List<ManageDTO.CommentReportDto> getCommentReport(String order);

    public List<ManageDTO.PostReportDto> getPostReport(String order);

    public void deleteMember(Long memberId);

    public void deleteCommentReport(Long commentId);

    public void deletePostReport(Long sellPostId);
}
