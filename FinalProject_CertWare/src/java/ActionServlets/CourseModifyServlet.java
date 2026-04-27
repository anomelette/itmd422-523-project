/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ActionServlets;

import DBConnection.MyConnections;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author wvega
 */
@WebServlet(name = "CourseModifyServlet", urlPatterns = {"/CourseModifyServlet"})
public class CourseModifyServlet extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CourseModifyServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CourseModifyServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String step    = request.getParameter("step");
        String action  = request.getParameter("action");
        String courseID = request.getParameter("courseid");
        
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = MyConnections.getConnection();

            out.println("<html><body>");
            out.println("<h1>Technical Institute Certification Program</h1>");

            if ("lookup".equals(step)) {

                if (courseID == null || courseID.trim().isEmpty()) {
                    out.println("<h2 style='color:red;'>Please enter a Course ID.</h2>");
                    out.println("<a href='coursemodify.html'><button>Try Again</button></a>");
                    out.println("</body></html>");
                    return;
                }

                String checkSQL = "SELECT CourseID FROM Courses WHERE CourseID = ?";
                ps = conn.prepareStatement(checkSQL);
                ps.setString(1, courseID.trim());
                rs = ps.executeQuery();

                if (!rs.next()) {
                    out.println("<h2 style='color:red;'>No course found with Course ID: " + courseID + "</h2>");
                    out.println("<a href='coursemodify.html'><button>Try Again</button></a>");
                    out.println("</body></html>");
                    return;
                }

                rs.close();
                ps.close();

                String courseSQL = "SELECT CourseName, isActive FROM Courses WHERE CourseID = ?";
                ps = conn.prepareStatement(courseSQL);
                ps.setString(1, courseID.trim());
                rs = ps.executeQuery();
                rs.next();

                String courseName  = rs.getString("CourseName");
                String isActive    = rs.getString("isActive");

                rs.close();
                ps.close();

                String enrollSQL = "SELECT StudentID, EnrollDate, CompletionStatus FROM Enrollment WHERE CourseID = ?";
                ps = conn.prepareStatement(enrollSQL);
                ps.setString(1, courseID.trim());
                rs = ps.executeQuery();

                out.println("<h2>Course Found: " + courseName + " (ID: " + courseID + ")</h2>");
                out.println("<p><b>Active:</b> " + ("1".equals(isActive) ? "Yes" : "No") + "</p>");

                out.println("<h3>Enrolled Students</h3>");
                out.println("<form action='CourseModifyServlet' method='POST'>");
                out.println("<input type='hidden' name='courseid' value='" + courseID + "'>");
                out.println("<input type='hidden' name='action' value='updatestatus'>");
                out.println("<table border='1' cellpadding='8'>");

                boolean hasStudents = false;
                while (rs.next()) {
                    hasStudents = true;
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("StudentID") + "</td>");
                    out.println("<td>" + rs.getString("EnrollDate") + "</td>");
                    out.println("<td>");
                    out.println("<select name=\"status_" + rs.getString("StudentID") + "\">");
                    out.println("<option value=\"Enrolled\"" + ("Enrolled".equals(rs.getString("CompletionStatus")) ? " selected" : "") + ">Enrolled</option>");
                    out.println("<option value=\"Passed\""  + ("Passed".equals(rs.getString("CompletionStatus"))   ? " selected" : "") + ">Passed</option>");
                    out.println("<option value=\"Dropped\"" + ("Dropped".equals(rs.getString("CompletionStatus"))  ? " selected" : "") + ">Dropped</option>");
                    out.println("<option value=\"Failed\""  + ("Failed".equals(rs.getString("CompletionStatus"))   ? " selected" : "") + ">Failed</option>");
                    out.println("</select>");
                    out.println("</td>");
                    out.println("</tr>");
                    
                }
                out.println("</table>");
                
                if (hasStudents) {
                    out.println("<br><button type='submit'>Save Status Changes</button>");
                }
                
                out.println("</form>");
                
                if (!hasStudents) {
                    out.println("<p>No students are currently enrolled in this course.</p>");
                }

                out.println("<h3>What would you like to do?</h3>");
                out.println("<div style='display:flex; gap:10px;'>");

                out.println("<form action='CourseModifyServlet' method='POST'>");
                out.println("<input type='hidden' name='courseid' value='" + courseID + "'>");
                out.println("<button type='submit'>Modify</button>");
                out.println("</form>");

                out.println("<form action='CourseModifyServlet' method='POST'>");
                out.println("<input type='hidden' name='courseid' value='" + courseID + "'>");
                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<button type='submit' onclick=\"return confirm('Are you sure you want to delete this course? This action cannot be undone.');\">Remove</button>");
                out.println("</form>");
                
                out.println("<a href='coursemodify.html'><button type='button'>Back</button></a>");

                out.println("</div>");
            }

            else if ("edit".equals(step)) {

                String sql = "SELECT CourseName, isActive FROM Courses WHERE CourseID = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, courseID);
                rs = ps.executeQuery();

                if (rs.next()) {
                    String currentName     = rs.getString("CourseName");
                    String currentIsActive = rs.getString("isActive");

                    out.println("<h2>Edit Course</h2>");

                    out.println("<form action='CourseModifyServlet' method='POST'>");
                    out.println("<input type='hidden' name='courseid' value='" + courseID + "'>");
                    out.println("<input type='hidden' name='action' value='update'>");

                    out.println("Course Name:<br>");
                    out.println("<input name='coursename' value='" + currentName + "'><br><br>");

                    out.println("Active:<br>");
                    out.println("<select name='isActive'>");
                    out.println("<option value='1'" + ("1".equals(currentIsActive) ? " selected" : "") + ">Yes</option>");
                    out.println("<option value='0'" + ("0".equals(currentIsActive) ? " selected" : "") + ">No</option>");
                    out.println("</select><br><br>");

                    out.println("<button type='submit'>Save Changes</button>");
                    out.println("</form>");
                }
            }

            else if ("delete".equals(action)) {

                String sql = "DELETE FROM Courses WHERE CourseID = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, courseID);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Course Deleted Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Course not found</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
            }

            else if ("update".equals(action)) {

                String sql = "UPDATE Courses SET CourseName = ?, isActive = ? WHERE CourseID = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, request.getParameter("coursename"));
                ps.setString(2, request.getParameter("isActive"));
                ps.setString(3, courseID);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    out.println("<h2>Course Updated Successfully</h2>");
                } else {
                    out.println("<h2 style='color:red;'>Update failed</h2>");
                }

                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='coursemodify.html'><button>Modify/Remove Another Course</button></a>");
            }
            else if ("updatestatus".equals(action)) {
                String sql = "UPDATE Enrollment SET CompletionStatus = ? WHERE CourseID = ? AND StudentID = ?";
                ps = conn.prepareStatement(sql);

                String fetchSQL = "SELECT StudentID FROM Enrollment WHERE CourseID = ?";
                PreparedStatement fetchPs = conn.prepareStatement(fetchSQL);
                fetchPs.setString(1, courseID);
                rs = fetchPs.executeQuery();

                int updatedCount = 0;
                while (rs.next()) {
                    String sid = rs.getString("StudentID");
                    String newStatus = request.getParameter("status_" + sid);
                    if (newStatus != null) {
                        ps.setString(1, newStatus);
                        ps.setString(2, courseID);
                        ps.setString(3, sid);
                        ps.executeUpdate();
                        updatedCount++;
                    }
                }
                fetchPs.close();

                out.println("<h2>Updated " + updatedCount + " enrollment(s) successfully.</h2>");
                out.println("<a href='menu.html'><button>Back to Menu</button></a>");
                out.println("<a href='coursemodify.html'><button>Modify Another Course</button></a>");
            }
            else {
                out.println("<h2>Invalid request</h2>");
                out.println("<p>Step: " + step + "</p>");
                out.println("<p>Action: " + action + "</p>");
                out.println("<a href='coursemodify.html'><button>Try Again</button></a>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2 style='color:red;'>Error occurred</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='coursemodify.html'><button>Try Again</button></a>");
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
