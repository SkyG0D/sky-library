package sky.skygod.skylibrary.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("basic-security")
@Configuration
@EnableSwagger2
public class BasicSwaggerConfig {

    @Bean
    public Docket skyLibraryApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(SwaggerConfigInfo.getApis())
            .build()
            .apiInfo(SwaggerConfigInfo.getApiInfo());
    }

}
