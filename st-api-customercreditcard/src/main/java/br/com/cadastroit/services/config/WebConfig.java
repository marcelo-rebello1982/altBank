package br.com.cadastroit.services.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .favorPathExtension(false)  
            .favorParameter(false)      
            .ignoreAcceptHeader(true)   
            .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON); // Força JSON como padrão
    }
}
