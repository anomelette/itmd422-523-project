package ActionServlets;

import DBConnection.MyConnections;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ayen
 */
@WebServlet(name = "CourseAddServlet", urlPatterns = {"/CourseAddServlet"})
public class CourseAddServlet extends HttpServlet {

    /**
     * Handles HTTP POST (form submission)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String coursename = request.getParameter("coursename");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = MyConnections.getConnection();
            String sql = "INSERT INTO Courses (CourseName, isActive) VALUES (?, 1)";
            ps = conn.prepareStatement(sql);

            ps.setString(1, coursename);
            ps.executeUpdate();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2>Course added successfully!</h2>");

                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("<br><br>");

                out.println("<a href='courseadd.html'>");
                out.println("<button>Add Another Course</button>");
                out.println("</a>");

                out.println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2 style='color:red;'>Error adding course</h2>");
                out.println("<p><b>Details:</b> " + e.getMessage() + "</p>");

                out.println("<a href='courseadd.html'>");
                out.println("<button>Try Again</button>");
                out.println("</a>");

                out.println("<br><br>");

                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("</body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2 style='color:red;'>Unexpected Error</h2>");
                out.println("<p>" + e.getMessage() + "</p>");

                out.println("<a href='courseadd.html'>");
                out.println("<button>Try Again</button>");
                out.println("</a>");

                out.println("<br><br>");

                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("</body></html>");
            }
        } finally {

            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}