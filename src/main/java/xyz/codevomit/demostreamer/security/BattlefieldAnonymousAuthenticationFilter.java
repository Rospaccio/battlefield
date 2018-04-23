package xyz.codevomit.demostreamer.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;


@Component
public class BattlefieldAnonymousAuthenticationFilter extends
	AbstractAuthenticationProcessingFilter  {
	
	@Value("#{'${xyz.codevomit.demostreamer.anonymous.roles:}'.split(',')}")
	private List<String> roles = new ArrayList<String>();
	
	
	public BattlefieldAnonymousAuthenticationFilter(){
		super(new AntPathRequestMatcher("/login", "POST"));
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		String userName = StringUtils.trim(request.getParameter("username"));
		if(!StringUtils.isBlank(userName)){
			return makeAuthentication(userName, request);
		}
		
		return null;
	}
	
	@Override
	public void afterPropertiesSet() {
		// disable "remember-me" service...
		this.setRememberMeServices(new NullRememberMeServices());
	}	
	
	private Authentication makeAuthentication(String userName, HttpServletRequest request){
		
		// autorities
		List<GrantedAuthority> autorities = new ArrayList<GrantedAuthority>();
		for(String r : roles){
			autorities.add(new SimpleGrantedAuthority(r));
		}
		
		// token
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				userName, UUID.randomUUID().toString(), autorities);
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
		
		return authRequest;
	}
}
