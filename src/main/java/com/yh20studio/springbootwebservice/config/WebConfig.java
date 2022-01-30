package com.yh20studio.springbootwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	// localHost Web에서 들어오는 접근을 허용하기 위함.
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://127.0.0.1:8000/")
			.allowedOrigins("https://yh20studio.github.io/flutter_new_calry/")
			.allowedMethods("POST", "GET", "DELETE", "PUT");
	}

	// REST API에 대한 접근 및 리턴이 실행될때 Log 값을 적기 위한 Bean 생성.
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter c = new CommonsRequestLoggingFilter();
		c.setIncludeHeaders(true);
		c.setIncludeQueryString(true);
		c.setIncludePayload(true);
		c.setIncludeClientInfo(true);
		c.setMaxPayloadLength(1000);
		return c;
	}
}
