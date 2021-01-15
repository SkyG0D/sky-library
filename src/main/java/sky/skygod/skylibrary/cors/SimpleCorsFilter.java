package sky.skygod.skylibrary.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sky.skygod.skylibrary.property.SecurityProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SimpleCorsFilter implements Filter {

    private final SecurityProperties securityProperties;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String allowedOrigin = securityProperties.getAllowedOrigin();
        String allowedMethods = String.join(", ", securityProperties.getAllowedMethods()).toUpperCase();

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", allowedMethods);
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        if (request.getMethod().equals("OPTIONS") && (allowedOrigin.equals("*") ||
                allowedOrigin.equals(request.getHeader("Origin")))) {

            response.setStatus(HttpServletResponse.SC_OK);

        } else {
            chain.doFilter(req, res);
        }

    }

}
