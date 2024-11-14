package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;
import mandooparty.mandoo.domain.enums.MemberStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Builder.Default
    private Boolean isLogin=false;

    //private LocalDate inactiveDate;
    @Builder.Default
    private Integer writeSellPostCount=0;

    @Builder.Default
    private Integer likeSellPostCount = 0;

    @Builder.Default
    private Integer completedSellPostCount = 0;

    public void setLoginStatus(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
