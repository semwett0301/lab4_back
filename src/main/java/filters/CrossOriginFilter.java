package filters;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;


import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter(urlPatterns = "*")
public class CrossOriginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Before");
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Methods", "*");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
        chain.doFilter(request, resp);
        System.out.println("After");
    }

    @Override
    public void destroy() {

    }
}
