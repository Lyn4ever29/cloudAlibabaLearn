package cn.lyn4ever.learn.nacosserver8002;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosServer8002Application {

	public static void main(String[] args) {
		SpringApplication.run(NacosServer8002Application.class, args);
	}

}
