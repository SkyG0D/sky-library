package sky.skygod.skylibrary;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Log4j2
public class SkyLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyLibraryApplication.class, args);
    }

}
