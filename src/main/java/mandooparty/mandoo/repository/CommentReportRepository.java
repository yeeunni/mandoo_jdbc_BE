package mandooparty.mandoo.repository;

import mandooparty.mandoo.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport,Long> {

    Optional<CommentReport> findByCommentAndMember(Comment comment, Member member);

    CommentReport findByComment(Comment comment);
}
