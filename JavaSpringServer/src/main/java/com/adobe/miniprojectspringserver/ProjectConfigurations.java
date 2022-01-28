

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfigurations {

    @Bean
    public Docket api() {

                .paths(PathSelectors.any()).build();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityConfigurerAdapter corsConfigurer() {
        return new WebSecurityConfigurerAdapter() {

            private UserDetailsService userDetailService;

            @Qualifier("userServiceImpl")
            @Autowired
            public void setUserDetailService(UserDetailsService userDetailService) {
                this.userDetailService = userDetailService;
            }

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailService).passwordEncoder(encodePwd());
            }

            @Bean
            CorsConfigurationSource corsConfigurationSource() {
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
                return source;
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.cors().and().csrf().disable().authorizeRequests()
                        .and()
                        .authorizeRequests()
                        .antMatchers("/V2/**", "/V1/**").authenticated().anyRequest().permitAll()
                        .and()
                        .addFilter(new AuthenticationFilter(authenticationManager()))
                        .addFilter(new AuthorizationFilter(authenticationManager()))
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .and()
                        .httpBasic();
            }
        };
    }
}