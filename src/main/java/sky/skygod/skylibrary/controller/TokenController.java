package sky.skygod.skylibrary.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sky.skygod.skylibrary.property.SecurityProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Api(value = "Token")
public class TokenController {

    private final SecurityProperties securityProperties;

    @ApiOperation(value = "Logout token")
    @DeleteMapping("/revoke")
    public ResponseEntity<Void> revoke(HttpServletRequest req, HttpServletResponse res) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(securityProperties.isEnableHttps());
        cookie.setPath(req.getContextPath() + "/oauth/token");
        cookie.setMaxAge(0);

        res.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

}
