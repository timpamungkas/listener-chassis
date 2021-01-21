package id.co.bfi.listenerchassis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ListenerchassisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListenerchassisApplication.class, args);
	}

}
