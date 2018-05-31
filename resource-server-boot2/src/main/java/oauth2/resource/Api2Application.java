package oauth2.resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@EnableResourceServer
@SpringBootApplication
public class Api2Application {

    @RestController
    class SecurityController {

        @GetMapping("/userinfo")
        public Principal currentUserName(Principal principal) {
            return principal;
        }
    }

    @Bean
    public ResourceServerConfigurerAdapter resourceServerConfigurerAdapter() {
        return new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.headers().frameOptions().disable();
                http.authorizeRequests()
                        .antMatchers("/members", "/members/**").access("#oauth2.hasScope('member.info.public')")
                        .anyRequest().authenticated();
            }
        };
    }

//    @Bean
//    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
//                                                 OAuth2ProtectedResourceDetails details) {
//        return new OAuth2RestTemplate(details, oauth2ClientContext);
//    }

    /**
     * API를 조회시 출력될 테스트 데이터
     * @param memberRepository
     * @return
     */
    @Bean
    public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
        return args -> {
            memberRepository.save(new Member("이철수", "chulsoo", "test111"));
            memberRepository.save(new Member("김정인", "jungin11", "test222"));
            memberRepository.save(new Member("류정우", "jwryu991", "test333"));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Api2Application.class, args);
    }
}

