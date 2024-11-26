package mandooparty.mandoo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mandooparty.mandoo.domain.enums.SellPostStatus;

import java.time.LocalDate;

public class MyPageDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberIdRequestDto{//myPage request
        private Long memberId;
    }

}
