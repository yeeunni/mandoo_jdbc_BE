package mandooparty.mandoo.service.ReportsService;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.PostReport;

public interface ReportService {

    public PostReport createSellPostReport(Long sellPostId, Long memberId);

    public CommentReport createCommentReport(Long commentId, Long memberId);
}
