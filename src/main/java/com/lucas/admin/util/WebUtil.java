package com.lucas.admin.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hyl on 2019/5/10.
 */
public class WebUtil {
    private static Logger logger = Logger.getLogger(WebUtil.class);

    public WebUtil() {
    }

    public static void print(HttpServletResponse response, String content) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try {
            PrintWriter out = response.getWriter();
            logger.debug(content);
            out.print(content);
            out.flush();
            out.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public static String getWebRoot(HttpServletRequest request, String realPath) {
        return request.getSession().getServletContext().getRealPath(realPath);
    }
}
