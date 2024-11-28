package mandooparty.mandoo.service.ManageService;


import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import mandooparty.mandoo.converter.ManageConverter;
import mandooparty.mandoo.domain.*;
import mandooparty.mandoo.exception.GlobalErrorCode;
import mandooparty.mandoo.exception.GlobalException;
import mandooparty.mandoo.repository.*;
import mandooparty.mandoo.web.dto.ManageDTO;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManageServiceImpl implements ManageService {
    private final SellPostRepository sellPostRepository;
    private final SellPostCategoryRepository sellPostCategoryRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final PostReportRepository postReportRepository;
    private final LikesRepository likesRepository;
    public List<ManageDTO.ManageDashBoardSellPostDto> getDaySellPostCount(){
        List<Map<String, Object>> result = sellPostRepository.countByCreatedAt();

        // Tupleìì ë°ì´í°ë¥¼ êº¼ë´ì ManageDashBoardSellPostDtoë¡ ë³í
        List<ManageDTO.ManageDashBoardSellPostDto> dtoList = new ArrayList<>();

        for (Tuple tuple : result) {
            // Tupleìì ê°ì ì¶ì¶
            LocalDate date = tuple.get(0, java.sql.Date.class).toLocalDate();  // ì²« ë²ì§¸ ê°ì ë ì§
            Long sellPostCount = tuple.get(1, Long.class);   // ë ë²ì§¸ ê°ì sellpost ê°ì

            // DTO ê°ì²´ ìì±íì¬ ë¦¬ì¤í¸ì ì¶ê°
            ManageDTO.ManageDashBoardSellPostDto dto = new ManageDTO.ManageDashBoardSellPostDto();
            dto.setDate(date);  // ë ì§ ì¤ì 
            dto.setSellPostCount(sellPostCount.intValue());  // ê°ì ì¤ì  (Long -> Integerë¡ ë³í)

            dtoList.add(dto);  // ë¦¬ì¤í¸ì ì¶ê°
        }

        return dtoList;

    }

    public List<ManageDTO.ManageDashBoardCategoryRatioDto> getCategoryRatio()
    {
        List<Tuple> result=sellPostCategoryRepository.countCategory();


        // Tupleìì ë°ì´í°ë¥¼ êº¼ë´ì ManageDashBoardSellPostDtoë¡ ë³í
        List<ManageDTO.ManageDashBoardCategoryRatioDto> dtoList = new ArrayList<>();

        for (Tuple tuple : result) {
            // Tupleìì ê°ì ì¶ì¶
            String name = tuple.get(0, String.class);  // ì²« ë²ì§¸ ê°ì ë ì§
            Long categoryCount = tuple.get(1, Long.class);   // ë ë²ì§¸ ê°ì sellpost ê°ì
            Double ratio = tuple.get(2, Double.class);

            // DTO ê°ì²´ ìì±íì¬ ë¦¬ì¤í¸ì ì¶ê°
            ManageDTO.ManageDashBoardCategoryRatioDto dto = new ManageDTO.ManageDashBoardCategoryRatioDto();
            dto.setName(name);  // ë ì§ ì¤ì 
            dto.setCategoryCount(categoryCount.intValue());  // ê°ì ì¤ì  (Long -> Integerë¡ ë³í)

            dto.setRatio((int) Math.round(ratio));



            dtoList.add(dto);  // ë¦¬ì¤í¸ì ì¶ê°
        }

        return dtoList;
    };

    public List<ManageDTO.ManageDashBoardDateViewDto> getDateView()
    {
        LocalDate today = LocalDate.now();
        List<ManageDTO.ManageDashBoardDateViewDto> dtoList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
//            LocalDateTime startOfDay=today.atStartOfDay();
//            LocalDateTime endOfDay=startOfDay.plusDays(1);
            Integer subscriber=memberRepository.getCountByDate(today).intValue();
            Integer sellPost=sellPostRepository.getCountByDate(today).intValue();
            Integer comment=commentRepository.getCountByDate(today).intValue();
            ManageDTO.ManageDashBoardDateViewDto dto=new ManageDTO.ManageDashBoardDateViewDto();

            dto.setDate(today);
            dto.setSubscriber(subscriber);
            dto.setSellPost(sellPost);
            dto.setComment(comment);
            dtoList.add(dto);
            today = today.minusDays(1);// ë ì§ë¥¼ íë£¨ ì¤ì´ê¸°
        }
        return dtoList;
    }


    public List<Member> getMember(String order){
        LocalDate today=LocalDate.now();
        LocalDate sixMonthsAgo=today.minusMonths(6);
        Sort sort=Sort.by(Sort.Direction.ASC,order);
        return memberRepository.findByLoginTime(sixMonthsAgo,sort);
    };

    public List<ManageDTO.CommentReportDto> getCommentReport(String order)
    {

        if ("created_at".equals(order)) {
            order = "createdAt";
        }

        Sort sort = Sort.by(Sort.Direction.ASC, order);
        List<CommentReport> commentReportList=commentReportRepository.findAll(sort);

        List<ManageDTO.CommentReportDto> commentReportDtoList=new ArrayList<>();
        for(CommentReport commentReport:commentReportList){
            commentReportDtoList.add(ManageConverter.ManageCommentReportDto(commentReport));
        }
        return commentReportDtoList;

    }

    public List<ManageDTO.PostReportDto> getPostReport(String order)
    {
        if ("created_at".equals(order)) {
            order = "createdAt";
        }

        Sort sort = Sort.by(Sort.Direction.ASC, order);
        List<PostReport> postReportList=postReportRepository.findAll(sort);

        List<ManageDTO.PostReportDto> postReportDtoList=new ArrayList<>();
        for(PostReport postReport:postReportList){
            postReportDtoList.add(ManageConverter.ManagePostReportDto(postReport));
        }
        return postReportDtoList;

    }

    public void deleteMember(Long memberId){
        Optional<Member> deleteMember=memberRepository.findById(memberId);

        if(deleteMember.isPresent()) {
            memberRepository.deleteById(memberId);
        }else{
            throw new GlobalException(GlobalErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public void deleteCommentReport(Long commentId){
        Optional<Comment> deleteComment=commentRepository.findById(commentId);

        if(deleteComment.isPresent()) {
            commentRepository.delete(deleteComment.get());
        }else{
            throw new GlobalException(GlobalErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void deletePostReport(Long sellPostId){
        Optional<SellPost> deletePost=sellPostRepository.findById(sellPostId);
        if(deletePost.isPresent()) {
//            PostReport postReport=postReportRepository.findBySellPost(deletePost.get());
//            postReportRepository.delete(postReport);
            sellPostRepository.deleteById(sellPostId);
        }else{
            throw new GlobalException(GlobalErrorCode.POST_NOT_FOUND);
        }
    }



}
