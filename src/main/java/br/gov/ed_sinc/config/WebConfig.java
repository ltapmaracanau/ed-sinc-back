package br.gov.ed_sinc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedMethods("*")
        .allowedOrigins("*")
        //.allowedOrigins(allowedOriginsArray) 
        .allowedHeaders("*")
        .maxAge(3600);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry
        .addResourceHandler("/images/**") 
        .addResourceLocations("file:/home/edsinc/assetstore/imagens")
        .setCachePeriod(0);
		}
		
}