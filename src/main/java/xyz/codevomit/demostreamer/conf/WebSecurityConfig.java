package xyz.codevomit.demostreamer.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import xyz.codevomit.demostreamer.security.BattlefieldAnonymousAuthenticationFilter;

/**
 *
 * @author merka
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	
	@Autowired
	private BattlefieldAnonymousAuthenticationFilter battlefieldAnonymousAuthenticationFilter;
	
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		// remove some static resources from security filters
		// TODO: probably it is better to remove some folders from the project
		web.ignoring().antMatchers("/js/**", "/lib/**", "/examples/**", "/script/**", "/audio/**", "/pics/**", "/favicon.ico");
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
		http
			.antMatcher("/**")
			.addFilterAfter(battlefieldAnonymousAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
			.authorizeRequests()
			.antMatchers("/", "/index.html", "/login", "/logout").permitAll()
			.anyRequest().authenticated()
		.and()
			.exceptionHandling()
		.and()
			.formLogin()
			.loginPage("/login")
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/");
    }

}
