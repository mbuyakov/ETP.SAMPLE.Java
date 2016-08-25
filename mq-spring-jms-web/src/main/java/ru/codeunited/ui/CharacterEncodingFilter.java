package ru.codeunited.ui;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Natalia Andrianova on 23.08.2016.
 */
public class CharacterEncodingFilter implements Filter {

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (this.encoding != null) {
            servletRequest.setCharacterEncoding(this.encoding);
            servletResponse.setCharacterEncoding(this.encoding);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
