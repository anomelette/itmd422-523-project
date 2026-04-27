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

        String studentid  = request.getParameter("studentid");
        String certid     = request.getParameter("certid");
        String issuedate  = request.getParameter("issuedate");
        String expirydate = request.getParameter("expirydate");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            
            conn = MyConnections.getConnection();
            
            String sql = "INSERT INTO StudentCertifications (StudentID, CertID, IssueDate, ExpiryDate) VALUES (?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(studentid));
            ps.setInt(2, Integer.parseInt(certid));
            ps.setDate(3, java.sql.Date.valueOf(issuedate));
            
            if (expirydate != null && !expirydate.trim().isEmpty()) {
                ps.setDate(4, java.sql.Date.valueOf(expirydate));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            ps.executeUpdate();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<h2>Student certification added successfully!</h2>");
                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='studentcertificationadd.html'><button>Add Another</button></a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<h2 style='color:red;'>Error adding student certification</h2>");
                out.println("<p><b>Details:</b> " + e.getMessage() + "</p>");
                out.println("<a href='studentcertificationadd.html'><button>Try Again</button></a>");
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
                out.println("<a href='studentcertificationadd.html'><button>Try Again</button></a>");
                out.println("<br><br>");
                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
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