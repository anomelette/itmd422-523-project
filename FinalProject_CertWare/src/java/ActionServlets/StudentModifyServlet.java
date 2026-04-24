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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = MyConnections.getConnection();

            out.println("<html><body>");

            // =========================
            // STEP 1: LOOKUP STUDENT
            // =========================
            if ("lookup".equals(step)) {

                String sql = "SELECT * FROM Student WHERE StudentID=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(studentid));
                rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<h2>Student Found</h2>");
                                        
                    // back button
                    out.println("<a href='studentmodify.html'>");
                    out.println("<button type='button'>Back</button>");
                    out.println("</a>");  
                    
                    out.println("<p><b>Name:</b> " + rs.getString("FirstName") + " " + rs.getString("LastName") + "</p>");
                    out.println("<p><b>Email:</b> " + rs.getString("Email") + "</p>");
                    out.println("<p><b>DOB:</b> " + rs.getDate("DOB") + "</p>");

                    out.println("<h3>What would you like to do?</h3>");

                    out.println("<div style='display:flex; gap:10px;'>");
                    
                    // update button
                    out.println("<form action='StudentModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='studentid' value='" + studentid + "'>");
                    out.println("<input type='hidden' name='step' value='edit'>");
                    out.println("<button type='submit'>Modify</button>");
                    out.println("</form><br>");

                    // remove button
                    out.println("<form action='StudentModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='studentid' value='" + studentid + "'>");
                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<button type='submit' onclick=\"return confirm('Are you sure you would like to remove this student profile? This action cannot be undone.');\">Remove</button>");
                    out.println("</form>");
                    out.println("</div>");                  

                } else {
                    out.println("<h2 style='color:red;'>Student not found</h2>");
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