package sky.skygod.skylibrary.configuration;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;

import java.util.function.Predicate;

public class SwaggerConfigInfo {

    public static ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
            .title("SkyLibrary REST API")
            .description("SkyLibrary is an api for libraries developed with spring boot.")
            .version("1.0.0")
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .build();
    }

    public static Predicate<RequestHandler> getApis() {
        return RequestHandlerSelectors.basePackage("sky.skygod.skylibrary");
    }

}
