package cn.lyn4ever.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosServer8001Application {

	public static void main(String[] args) {
		SpringApplication.run(NacosServer8001Application.class, args);
	}

}
