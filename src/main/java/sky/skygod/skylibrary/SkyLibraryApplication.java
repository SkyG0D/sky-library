package sky.skygod.skylibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class SkyLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyLibraryApplication.class, args);
    }

}
