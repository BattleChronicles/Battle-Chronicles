package fr.battlechronicles;

import java.io.IOException;
import javax.servlet.http.*;


//ne sert à rien
@SuppressWarnings("serial")
public class BCServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, world");
    }
}
