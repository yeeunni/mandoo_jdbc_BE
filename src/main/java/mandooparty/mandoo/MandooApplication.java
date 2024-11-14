package mandooparty.mandoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //엔티티가 처음 저장될때 수정될 때마다 자동으로 현재 시간을 기록한다.
public class MandooApplication {

	public static void main(String[] args) {
		SpringApplication.run(MandooApplication.class, args);
	}

}
