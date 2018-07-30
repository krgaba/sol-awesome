package com.sol.awesome.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleCORSFilter implements Filter {
	
	public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	
	@Value("${cors.accessControlOrigin:*}")
	private String accessControlOrigin;
	@Value("${cors.accessControlMaxAge:1800}")
	private String accessControlMaxAge;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, accessControlOrigin);
		response.setHeader(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader(RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE, accessControlMaxAge);
		response.setHeader(RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
		chain.doFilter(req, res);
	}
	
	public void init(FilterConfig filterConfig) {
	
	}
	
	public void destroy() {
	
	}
}

