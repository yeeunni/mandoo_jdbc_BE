package mandooparty.mandoo.service.ReportsService;

import mandooparty.mandoo.domain.Comment;
import mandooparty.mandoo.domain.CommentReport;
import mandooparty.mandoo.domain.PostReport;

import java.util.Map;

public interface ReportService {

    public PostReport createSellPostReport(Long sellPostId, Long memberId);

    public Map<String,Object> createCommentReport(Long commentId, Long memberId);
}
