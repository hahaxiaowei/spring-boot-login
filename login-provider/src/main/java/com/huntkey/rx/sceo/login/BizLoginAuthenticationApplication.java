package com.huntkey.rx.sceo.login;

import com.huntkey.rx.sceo.method.register.plugin.annotation.EnableDriverMethod;
import com.huntkey.rx.sceo.method.register.plugin.annotation.EnableMethodRegisterScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.huntkey.rx"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableMethodRegisterScanner(startApplicationClass = BizLoginAuthenticationApplication.class,
							serviceApplicationName = "${spring.application.name}")
@EnableDriverMethod
public class BizLoginAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizLoginAuthenticationApplication.class, args);
	}
}
