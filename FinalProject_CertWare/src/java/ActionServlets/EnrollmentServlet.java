/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ActionServlets;

import DBConnection.MyConnections;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author wvega
 */
@WebServlet(name = "EnrollmentServlet", urlPatterns = {"/EnrollmentServlet"})
public class EnrollmentServlet extends HttpServlet {

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
            out.println("<title>Servlet EnrollmentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EnrollmentServlet at " + request.getContextPath() + "</h1>");
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
        
        String studentid = request.getParameter("coursename");
        String courseid = request.getParameter("isActive");
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = MyConnections.getConnection();
            String sql = "INSERT INTO Enrollment (StudentID, CourseID, EnrollDate) VALUES (?, ?, NOW())";
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentid);
            ps.setString(2, courseid);
            ps.executeUpdate();
            
            response.setContentType("text/html;charset=UTF-8");
            
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");

                out.println("<h2>Student enrolled successfully!</h2>");

                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("<br><br>");

                out.println("<a href='enrollment.html'>");
                out.println("<button>Enroll another student</button>");
                out.println("</a>");

                out.println("</body></html>");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
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
