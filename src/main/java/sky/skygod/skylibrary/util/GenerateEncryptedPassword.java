package sky.skygod.skylibrary.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
public class GenerateEncryptedPassword {
    public static void main(String[] args) {
        String password = "SkyG0D";
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password: {}", passwordEncoder.encode(password));
    }
}
