package br.gov.ed_sinc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customAPI() {
		return new OpenAPI()
				/*
				.addSecurityItem(new SecurityRequirement().
			            addList("Bearer Authentication"))
				 */
				.addServersItem(new Server().url("https://ed-sinc-back-production.up.railway.app").description("Production server"))
			    .components(new Components().addSecuritySchemes
			            ("Bearer Authentication", createAPIKeyScheme()))
			           
				.info(new Info()
				.title("Ed-Sinc API")
				.version("1.0.0")
				.license(new License().name("Licença do Sistema").url("https://www.url.padrao.br/"))
				);
	}
	
	private SecurityScheme createAPIKeyScheme() {
	    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	        .bearerFormat("JWT")
	        .scheme("bearer");
	}
}
