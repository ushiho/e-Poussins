/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lotfi
 */
public class DownloadUtil {

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    public static void downloadFile(File fileToDownload) throws FileNotFoundException, IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Length", String.valueOf(fileToDownload.length()));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileToDownload.getName() + "\"");

        // Open streams.
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileToDownload), DEFAULT_BUFFER_SIZE);
        BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

        // Write file contents to response.
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Gently close streams.
        output.flush();
        output.close();
        input.close();
    }

}
