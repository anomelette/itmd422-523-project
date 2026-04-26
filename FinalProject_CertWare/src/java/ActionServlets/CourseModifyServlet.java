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
        processRequest(request, response);
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
        
        String courseID = request.getParameter("courseid");

        if (courseID == null || courseID.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/coursemodify.html");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Course Enrollment - " + courseID + "</title></head>");
            out.println("<body>");
            out.println("<h1>Technical Institute Certification Program</h1>");
            out.println("<h2>Enrolled Students for Course ID: " + courseID + "</h2>");

            try {
                conn = MyConnections.getConnection();
                String sql = "SELECT StudentID, EnrollDate FROM Enrollment WHERE CourseID = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, courseID);
                rs = ps.executeQuery();

                out.println("<table border='1'>");
                out.println("<tr><th>Student ID</th><th>Enroll Date</th></tr>");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("StudentID") + "</td>");
                    out.println("<td>" + rs.getString("EnrollDate") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

                if (!hasResults) {
                    out.println("<p>No students are currently enrolled in Course ID: " + courseID + "</p>");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<p>Database error: " + e.getMessage() + "</p>");
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            out.println("<br><a href='coursemodify.html'>Search another course</a>");
            out.println("</body>");
            out.println("</html>");
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
