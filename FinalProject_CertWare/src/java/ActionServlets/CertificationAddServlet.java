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
@WebServlet(name = "CertificationAddServlet", urlPatterns = {"/CertificationAddServlet"})
public class CertificationAddServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Certification Add Servlet</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String certname = request.getParameter("certname");
        String validmonths = request.getParameter("validmonths");
        String courseid = request.getParameter("courseid");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO certifications (CertName, ValidMonths, CourseID) VALUES (?, ?, ?)";
            
            conn = MyConnections.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, certname);
            ps.setInt(2, Integer.parseInt(validmonths));
            ps.setInt(3, Integer.parseInt(courseid));

            ps.executeUpdate();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2>Certification added successfully!</h2>");

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='certificationadd.html'><button>Add Another Certification</button></a>");

                out.println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2 style='color:red;'>Error adding certification</h2>");
                out.println("<p><b>Details:</b> " + e.getMessage() + "</p>");

                out.println("<a href='certificationadd.html'><button>Try Again</button></a>");

                out.println("<br><br>");


                out.println("<a href='menu.html'><button>Back to Menu</button></a>");

                out.println("</body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2 style='color:red;'>Unexpected Error</h2>");
                out.println("<p>" + e.getMessage() + "</p>");

                out.println("<a href='certificationadd.html'><button>Try Again</button></a>");

                out.println("<br><br>");

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");

                out.println("</body></html>");
            }
        }
    }
}