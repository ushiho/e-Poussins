/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author lotfi
 */
public class DownloadUtil {

    public static void download(File file, String fileName, String contentType) throws FileNotFoundException, IOException {

        System.out.println("hi from download");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType(contentType);
        externalContext.setResponseHeader("Content-Disposition", "attachement;filename=\"" + fileName + "\" ");

        try (FileInputStream inputStream = new FileInputStream(file)) {
            OutputStream outputStream = externalContext.getResponseOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        context.responseComplete();
    }
}
