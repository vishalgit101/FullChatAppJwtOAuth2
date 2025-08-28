package filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.JwtService;
import service.MyUserDetailsService;

@Component
public class JwtFilter extends OncePerRequestFilter { // every incoming request will go though this filter after we add it in the filter chain
	
	// DI Service and Context
	private final JwtService jwtService;
	private final ApplicationContext context;
	
	@Autowired
	public JwtFilter(JwtService jwtService, ApplicationContext context) {
		super();
		this.jwtService = jwtService;
		this.context = context;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// check headers
		String username = null;
		String authHeader = request.getHeader("Authorization");
		String token = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // start from index 7
			
			// now if the token wasnt in the header we'll check the cookies
			if(token == null) {
				Cookie [] cookies = request.getCookies();
				
				if(cookies != null) {
					for(Cookie cookie: cookies) {
						if("JWT".equals(cookie.getName())) {
							token = cookie.getValue();
						}
					}
				}
			}
			
			username = this.jwtService.extractUsername(token);
			
			
			
			// check if the user is not null or not already authenticated
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				// we are using the context we dont want the circular dependency here
				UserDetails userDetails = this.context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
				
				// validate the token 
				if(this.jwtService.validateToken(token, userDetails)) {
					// setting the user in the context
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					// the authToken should also be aware of the request object 
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

}
