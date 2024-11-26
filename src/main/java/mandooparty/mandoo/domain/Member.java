package mandooparty.mandoo.domain;

import jakarta.persistence.*;
import lombok.*;
import mandooparty.mandoo.domain.common.BaseEntity;
import mandooparty.mandoo.domain.enums.MemberStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDateTime loginTime;

    @Builder.Default
    private Integer completedSellPostCount = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellPost> sellPosts = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> Likes=new ArrayList<>();
    public void setLoginStatus(boolean isLogin) {
        this.isLogin = isLogin;
    }
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
