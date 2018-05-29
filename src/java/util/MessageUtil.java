/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author moulaYounes
 */
public class MessageUtil {

    public static void showMsgSelectToModify() {
        showMsg("SÉLÉCTIONNER UN UTILISATEUR POUR MODIFIER !");
    }

    public static void showMsgSelectToRemove() {
        showMsg("SÉLÉCTIONNER UN UTILISATEUR !");
    }

//    public static void saveMessage() {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        context.addMessage(null, new FacesMessage("Successful", "Your message: "));
//        context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
//    }

    public static void showMsg(String msg) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur", msg));
    }

    public static void info(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "Pas de détail"));
    }

    public static void warn(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, "Pas de détail"));
    }

    public static void error(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "Pas de détail"));
    }

    public static void fatal(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, "Pas de détail"));
    }

    public static void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
