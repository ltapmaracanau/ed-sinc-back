package br.gov.ed_sinc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "br.gov.ed_sinc.service")
public class ServiceConfig {

}
