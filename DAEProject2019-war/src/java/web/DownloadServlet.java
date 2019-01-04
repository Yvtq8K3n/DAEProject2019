/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Joao Marquez
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {

    
    String filename;
    String filepath;
    
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
        System.out.println("FINALY");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DownloadServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DownloadServlet at " + request.getContextPath() + "</h1>");
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
    public void doPost(HttpServletRequest req, HttpServletResponse response) {
        this.filename = req.getParameter("filename");
        this.filepath = req.getParameter("filepath");
        
 		HttpSession session = req.getSession(false);
 		if (session != null) {			
 			
                        response.setContentType("text/comma-separated-values");
                        response.setHeader("Content-Disposition", "attachment;filename=\""
 				+ filename + "\"");
 			download(response);
 		}		
 	}
 
 	private void download(HttpServletResponse response) {
 		BufferedWriter ow = null;
 		try {
 			BufferedReader br = new BufferedReader(new FileReader(filepath));
 			String line = br.readLine();
 
 			ow = new BufferedWriter(response.getWriter());
 			while (line != null) {
 				ow.write(line);
 				ow.newLine();
 				line = br.readLine();
 			}
 			ow.flush();
 		} catch (Exception exception) {
 			exception.printStackTrace();
 		} finally {
 			if (ow != null) {
                            try {
                                    ow.close();
                            } catch (IOException ioe) {
                                    ioe.printStackTrace();
                            }
 			}
 		}
 	}

}
