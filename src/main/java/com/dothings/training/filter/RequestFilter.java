package com.dothings.training.filter;


import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
@Order(Integer.MAX_VALUE)
public class RequestFilter implements Filter {


	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");
		//跨域设置,生产环境应该去掉
		httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		httpResponse.addHeader("Access-Control-Allow-Headers", "x-requested-with");
		httpResponse.addHeader("Access-Control-Max-Age", "3600");

		chain.doFilter(request, response);


		/*String url = httpRequest.getRequestURI().replaceFirst(httpRequest.getContextPath(), "");
		//	1.1）如果请求不需要登录状态，则直接放行
		if(SecurityMetadataSourceTrustListHolder.isTrustSecurityMetadataSource(url)){
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		String accessToken = httpRequest.getParameter("token");
		String userIdStr = httpRequest.getParameter("userId");
		Map<String ,Integer> rightToken = null;

		//	1.2）判断登陆状态：如果未登录则要求登录
		if(!StringUtils.hasTest(accessToken) || !StringUtils.hasTest(userIdStr)) {
			RestResp<String> rs = new RestResp<String>(ErrorCodes.USER_IS_NOT_LOGIN_ERROR.getErrorCode(), ErrorCodes.USER_IS_NOT_LOGIN_ERROR.getInfo(),0L);
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.getWriter().write(JsonUtils.toJson(rs));
			return;
		}

		String ehCacheAccessToken = CacheUtils.getCacheToString("u_" +userIdStr);
		if(!StringUtils.hasTest(ehCacheAccessToken)){
			RestResp<String> rs = new RestResp<String>(ErrorCodes.USER_ACCESSTOKEN_IS_NOT_ERROR.getErrorCode(),ErrorCodes.USER_ACCESSTOKEN_IS_NOT_ERROR.getInfo(),0L);
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.getWriter().write(JsonUtils.toJson(rs));
			return;
		}else{
			if(!ehCacheAccessToken.equals(accessToken)){
				RestResp<String> rs = new RestResp<String>(ErrorCodes.USER_ACCESSTOKEN_IS_NOT_ERROR.getErrorCode(),ErrorCodes.USER_ACCESSTOKEN_IS_NOT_ERROR.getInfo(),0L);
				httpResponse.setCharacterEncoding("UTF-8");
				httpResponse.getWriter().write(JsonUtils.toJson(rs));
				return;
			}
			chain.doFilter(request, response);
		}*/
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}



}
