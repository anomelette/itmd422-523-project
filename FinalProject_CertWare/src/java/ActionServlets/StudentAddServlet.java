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
@WebServlet(name = "StudentAddServlet", urlPatterns = {"/StudentAddServlet"})
public class StudentAddServlet extends HttpServlet {

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Student Add Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Student Add Servlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String dob = request.getParameter("dob");
        String studentid = request.getParameter("studentid");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // connect database
            conn = MyConnections.getConnection();

            // create SQL INSERT statement
            String sql = "INSERT INTO Student (StudentID, FirstName, LastName, Email, DOB) VALUES (?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);

            // set values
            ps.setInt(1, Integer.parseInt(studentid));
            ps.setString(2, fname);
            ps.setString(3, lname);
            ps.setString(4, email);
            ps.setDate(5, java.sql.Date.valueOf(dob));

            // execute insert
            ps.executeUpdate();

            // success page
            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Success</title></head><body>");

                out.println("<h2>Student added successfully!</h2>");

                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("</body></html>");
            }
            
        // Return error message
        } catch (SQLException e) {
            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Error</title></head><body>");

                out.println("<h2 style='color:red;'>Error adding student</h2>");

                out.println("<p><b>Details:</b> " + e.getMessage() + "</p>");

                out.println("<a href='studentadd.html'>");
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
                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Error</title></head><body>");

                out.println("<h2 style='color:red;'>Unexpected Error</h2>");
                out.println("<p>" + e.getMessage() + "</p>");

                out.println("<a href='studentadd.html'>");
                out.println("<button>Try Again</button>");
                out.println("</a>");

                out.println("<br><br>");
                out.println("<a href='menu.html'>");
                out.println("<button>Go to Menu</button>");
                out.println("</a>");

                out.println("</body></html>");
            }
        }
    }
}
