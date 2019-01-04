/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import dtos.ArtifactDTO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Joao Marquez
 */

@ManagedBean
@SessionScoped
@WebServlet(name="DownloadServlet", urlPatterns = "download")
public class DownloadServlet extends HttpServlet {

    private @Getter @Setter String filename;
    private @Getter @Setter String filepath;
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) {
        System.out.println("I WAS EXECUTED!");
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