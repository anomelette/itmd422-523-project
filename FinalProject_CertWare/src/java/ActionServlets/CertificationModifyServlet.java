package ActionServlets;

import DBConnection.MyConnections;
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/**
 *
 * @author ayen
 */
@WebServlet("/CertificationModifyServlet")
public class CertificationModifyServlet extends HttpServlet {

    /**
     * Handles HTTP POST requests (lookup, edit, update, delete)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");
        String action = request.getParameter("action");
        String certid = request.getParameter("certid");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = MyConnections.getConnection();

            out.println("<html><body>");

            if ("lookup".equals(step)) {

                String sql = "SELECT * FROM certifications WHERE CertID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(certid));
                rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<h2>Certification Found</h2>");

                    // back button
                    out.println("<a href='certificationmodify.html'><button type='button'>Back</button></a>");

                    // display details
                    out.println("<p><b>Name:</b> " + rs.getString("CertName") + "</p>");
                    out.println("<p><b>Valid Months:</b> " + rs.getInt("ValidMonths") + "</p>");
                    out.println("<p><b>Course ID:</b> " + rs.getInt("CourseID") + "</p>");

                    out.println("<h3>What would you like to do?</h3>");

                    // side-by-side buttons
                    out.println("<div style='display:flex; gap:10px;'>");

                    out.println("<form action='CertificationModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='certid' value='" + certid + "'>");
                    out.println("<input type='hidden' name='step' value='edit'>");
                    out.println("<button type='submit'>Modify</button>");
                    out.println("</form>");
                    
                    // remove button
                    out.println("<form action='CertificationModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='certid' value='" + certid + "'>");
                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<button type='submit' onclick=\"return confirm('Are you sure you want to delete this certification?');\">Remove</button>");
                    out.println("</form>");

                    out.println("</div>");

                } else {
                    out.println("<h2 style='color:red;'>Certification not found</h2>");
                    out.println("<a href='certificationmodify.html'><button>Try Again</button></a>");
                }
            }

            else if ("edit".equals(step)) {

                String sql = "SELECT * FROM certifications WHERE CertID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(certid));
                rs = ps.executeQuery();

                if (rs.next()) {

                    out.println("<h2>Edit Certification</h2>");

                    out.println("<form action='CertificationModifyServlet' method='POST'>");

                    out.println("<input type='hidden' name='certid' value='" + certid + "'>");
                    out.println("<input type='hidden' name='action' value='update'>");

                    out.println("Certification Name:<br>");
                    out.println("<input name='certname' value='" + rs.getString("CertName") + "'><br><br>");

                    out.println("Valid Months:<br>");
                    out.println("<input type='number' name='validmonths' value='" + rs.getInt("ValidMonths") + "'><br><br>");

                    out.println("Course ID:<br>");
                    out.println("<input type='number' name='courseid' value='" + rs.getInt("CourseID") + "'><br><br>");

                    out.println("<button type='submit'>Save Changes</button>");
                    out.println("</form>");
                }
            }

            else if ("delete".equals(action)) {

                String sql = "DELETE FROM certifications WHERE CertID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(certid));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Certification Deleted Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Certification not found</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
            }

            else if ("update".equals(action)) {

                String sql = "UPDATE certifications SET CertName=?, ValidMonths=?, CourseID=? WHERE CertID=?";
                ps = conn.prepareStatement(sql);

                ps.setString(1, request.getParameter("certname"));
                ps.setInt(2, Integer.parseInt(request.getParameter("validmonths")));
                ps.setInt(3, Integer.parseInt(request.getParameter("courseid")));
                ps.setInt(4, Integer.parseInt(certid));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Certification Updated Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Update failed</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='certificationmodify.html'><button>Modify Another Certification</button></a>");
            }

            else {
                out.println("<h2>Invalid request</h2>");
                out.println("<p>Step: " + step + "</p>");
                out.println("<p>Action: " + action + "</p>");
                out.println("<a href='certificationmodify.html'><button>Try Again</button></a>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();

            out.println("<h2 style='color:red;'>Error occurred</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='certificationmodify.html'><button>Try Again</button></a>");
        } finally {
            
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}