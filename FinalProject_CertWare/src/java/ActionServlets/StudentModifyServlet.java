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
@WebServlet("/StudentModifyServlet")
public class StudentModifyServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");
        String action = request.getParameter("action");
        String studentid = request.getParameter("studentid");
        String firstname = request.getParameter("firstname");
        String lastname  = request.getParameter("lastname");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = MyConnections.getConnection();

            out.println("<html><body>");

            if ("lookup".equals(step)) {
                
                boolean hasID    = studentid != null && !studentid.trim().isEmpty();
                boolean hasFirst = firstname != null && !firstname.trim().isEmpty();
                boolean hasLast  = lastname  != null && !lastname.trim().isEmpty();
                
                if (!hasID && !hasFirst && !hasLast) {
                    out.println("<h2 style='color:red;'>Please enter at least one search field.</h2>");
                    out.println("<a href='studentmodify.html'><button>Try Again</button></a>");
                    out.println("</body></html>");
                    return;
                }
                
                StringBuilder sql = new StringBuilder(
                    "SELECT s.StudentID, s.FirstName, s.LastName, s.Email, s.DOB, " +
                    "c.CertName, sc.IssueDate, sc.ExpiryDate " +
                    "FROM Student s " +
                    "LEFT JOIN StudentCertifications sc ON s.StudentID = sc.StudentID " +
                    "LEFT JOIN Certifications c ON sc.CertID = c.CertID " +
                    "WHERE 1=1"
                );

                if (hasID)    sql.append(" AND s.StudentID = ?");
                if (hasFirst) sql.append(" AND s.FirstName = ?");
                if (hasLast)  sql.append(" AND s.LastName = ?");

                ps = conn.prepareStatement(sql.toString());
                
                int index = 1;
                if (hasID)    ps.setInt(index++, Integer.parseInt(studentid.trim()));
                if (hasFirst) ps.setString(index++, firstname.trim());
                if (hasLast)  ps.setString(index++, lastname.trim());
                
                rs = ps.executeQuery();
                
                boolean studentFound = false;
                String foundStudentID = null;
                
                while (rs.next()) {

                    if (!studentFound) {
                        foundStudentID = rs.getString("StudentID");

                        out.println("<h2>Student Found</h2>");

                        out.println("<a href='studentmodify.html'><button type='button'>Back</button></a>");

                        out.println("<p><b>Name:</b> " + rs.getString("FirstName") + " " + rs.getString("LastName") + "</p>");
                        out.println("<p><b>Email:</b> " + rs.getString("Email") + "</p>");
                        out.println("<p><b>DOB:</b> " + rs.getDate("DOB") + "</p>");

                        // Certifications header
                        out.println("<h3>Certifications</h3>");
                        out.println("<table border='1' cellpadding='8'>");
                        out.println("<tr><th>Certification</th><th>Issue Date</th><th>Expiry Date</th></tr>");

                        studentFound = true;
                    }

                    if (rs.getString("CertName") != null) {
                        String expiry = rs.getString("ExpiryDate") != null ? rs.getString("ExpiryDate") : "N/A";
                        out.println("<tr>");
                        out.println("<td>" + rs.getString("CertName") + "</td>");
                        out.println("<td>" + rs.getString("IssueDate") + "</td>");
                        out.println("<td>" + expiry + "</td>");
                        out.println("</tr>");
                    }
                }

                if (studentFound) {

                    out.println("</table>");

                    out.println("<h3>What would you like to do?</h3>");
                    out.println("<div style='display:flex; gap:10px;'>");

                    out.println("<form action='StudentModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='studentid' value='" + foundStudentID + "'>");
                    out.println("<input type='hidden' name='step' value='edit'>");
                    out.println("<button type='submit'>Modify</button>");
                    out.println("</form>");

                    out.println("<form action='StudentModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='studentid' value='" + foundStudentID + "'>");
                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<button type='submit' onclick=\"return confirm('Are you sure you would like to remove this student profile? This action cannot be undone.');\">Remove</button>");
                    out.println("</form>");

                    out.println("</div>");

                } else {
                    out.println("<h2 style='color:red;'>No student found matching the provided details.</h2>");
                    out.println("<a href='studentmodify.html'><button>Try Again</button></a>");
                }
            }

            else if ("edit".equals(step)) {

                String sql = "SELECT * FROM Student WHERE StudentID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(studentid));
                rs = ps.executeQuery();

                if (rs.next()) {

                    out.println("<h2>Edit Student</h2>");

                    out.println("<form action='StudentModifyServlet' method='POST'>");

                    out.println("<input type='hidden' name='studentid' value='" + studentid + "'>");
                    out.println("<input type='hidden' name='action' value='update'>");

                    out.println("First Name:<br>");
                    out.println("<input name='fname' value='" + rs.getString("FirstName") + "'><br><br>");

                    out.println("Last Name:<br>");
                    out.println("<input name='lname' value='" + rs.getString("LastName") + "'><br><br>");

                    out.println("Email:<br>");
                    out.println("<input name='email' value='" + rs.getString("Email") + "'><br><br>");

                    out.println("DOB:<br>");
                    out.println("<input type='date' name='dob' value='" + rs.getDate("DOB") + "'><br><br>");

                    out.println("<button type='submit'>Save Changes</button>");
                    out.println("</form>");
                }
            }
            
            else if ("delete".equals(action)) {

                String sql = "DELETE FROM Student WHERE StudentID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(studentid));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Student Deleted Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Student not found</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
            }
            
            else if ("update".equals(action)) {

                String sql = "UPDATE Student SET FirstName=?, LastName=?, Email=?, DOB=? WHERE StudentID=?";
                ps = conn.prepareStatement(sql);

                ps.setString(1, request.getParameter("fname"));
                ps.setString(2, request.getParameter("lname"));
                ps.setString(3, request.getParameter("email"));
                ps.setDate(4, java.sql.Date.valueOf(request.getParameter("dob")));
                ps.setInt(5, Integer.parseInt(studentid));

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Student Updated Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Update failed</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='studentmodify.html'><button>Modify/Remove Another Student</button></a>");
            }

            else {
                out.println("<h2>Invalid request</h2>");
                out.println("<p>Step: " + step + "</p>");
                out.println("<p>Action: " + action + "</p>");
                out.println("<a href='studentmodify.html'><button>Try Again</button></a>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();

            out.println("<h2 style='color:red;'>Error occurred</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='studentmodify.html'><button>Try Again</button></a>");
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
