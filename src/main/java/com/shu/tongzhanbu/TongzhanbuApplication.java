package com.shu.tongzhanbu;


import com.shu.tongzhanbu.component.annotation.rest.AnonymousGetMapping;
import com.shu.tongzhanbu.component.util.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
@RestController
@EnableTransactionManagement
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class TongzhanbuApplication {

    public static void main(String[] args) {
        SpringApplication.run(TongzhanbuApplication.class, args);
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }


    @AnonymousGetMapping("/")
    public String index() {
        return "Backend service started successfully";}

}
