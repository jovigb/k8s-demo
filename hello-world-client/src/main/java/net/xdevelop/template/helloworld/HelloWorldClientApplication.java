package net.xdevelop.template.helloworld;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableAsync
@MapperScan(basePackages = {"net.xdevelop.template.helloworld.**.mapper"})
public class HelloWorldClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldClientApplication.class, args);
	}
}
