///*
package com.huntkey.rx.sceo.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huntkey.rx.commons.utils.eco.EcoSystemUtil;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HTTPBearerAuthorizeAttribute implements Filter {

    private static Logger logger = LoggerFactory.getLogger(HTTPBearerAuthorizeAttribute.class);

    @Autowired
    private LoginService loginService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("开始执行拦截器");
        Result result = new Result();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String auth = httpRequest.getHeader("Authorization");
        String uri = ((HttpServletRequest) request).getRequestURI();
        //jwt认证
        if (auth != null) {
            if (EcoSystemUtil.parseJWT(auth) != null) {
                chain.doFilter(request, response);
                return;
            }
        }
        //过滤除了登录，注册以外的请求
        if (uri.indexOf("/login") != -1
                && uri.indexOf("/login/getEcosystemSession") == -1
                && uri.indexOf("/login/clearCurrentStatus")== -1
                && uri.indexOf("/login/updateCurrentJobInfo")== -1
                || uri.indexOf("/register") != -1) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        result.setRetCode(Result.RECODE_UNLOGIN);
        result.setErrMsg("请重新登录！");
        httpResponse.getWriter().write(mapper.writeValueAsString(result));
        return;
    }


    @Override
    public void destroy() {

    }
}
//*/
