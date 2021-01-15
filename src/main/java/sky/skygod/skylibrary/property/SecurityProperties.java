package sky.skygod.skylibrary.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "my-security")
public class SecurityProperties {

    private String allowedOrigin = "*";
    private List<String> allowedMethods = Arrays.asList("POST", "DELETE", "PUT", "GET", "OPTIONS");

}
