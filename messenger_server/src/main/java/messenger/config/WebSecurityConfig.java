package messenger.config;

/**
 * Created by Narcis2007 on 09.12.2015.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/authenticate","/hello").permitAll()
                //.antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
                //.formLogin()
                //.loginPage("/login")
                //.permitAll()
                //.and()
                //.logout()
                //.permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,id from utilizatori where username=?")
                .authoritiesByUsernameQuery(
                        "select username, rol from roluri_utilizatori where username=?");
    }
}
