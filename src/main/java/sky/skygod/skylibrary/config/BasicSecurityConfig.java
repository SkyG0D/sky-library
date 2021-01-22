package sky.skygod.skylibrary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import sky.skygod.skylibrary.service.LibraryUserDetailsService;

@EnableWebSecurity
@RequiredArgsConstructor
@Profile("basic-security")
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LibraryUserDetailsService libraryUserDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/swagger-ui/**",
            "/**/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/**/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.userDetailsService(libraryUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
