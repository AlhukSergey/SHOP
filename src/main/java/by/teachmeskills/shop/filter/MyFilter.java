package by.teachmeskills.shop.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, initParams = {@WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding param")})
public class MyFilter implements Filter {
    private String code;

    @Override
    public void init(FilterConfig filterConfig) {
        code = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (code != null) {
            servletResponse.setContentType("text/html; charset=" + code);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        code = null;
    }
}
