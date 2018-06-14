package util;

import bean.Utilisateur;
//import controller.UtilisateurController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final SessionUtil instance = new SessionUtil();
    private static final List<Utilisateur> users = new ArrayList();

    private SessionUtil() {
    }

    public static void registerUser(Utilisateur user) {
        setAttribute("user", user);
        if (!isConnected(user)) {
            users.add(user);
        }
    }

    private static boolean isConnected(Utilisateur user) {
        return users.stream().anyMatch((existe) -> (existe.getLogin().equals(user.getLogin())));
    }

    public static void setAttribute(String cle, Object valeur) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null && fc.getExternalContext() != null) {
            getSession(fc).setAttribute(cle, valeur);
        }
    }

    private static HttpSession getSession(FacesContext fc) {
        return (HttpSession) fc.getExternalContext().getSession(false);
    }

    public static Object getAttribute(String cle) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Object res = null;
        if (isContextOk(fc)) {
            res = getSession(fc).getAttribute(cle);
        }
        return res;
    }

    private static boolean isContextOk(FacesContext fc) {
        boolean res = (fc != null
                && fc.getExternalContext() != null
                && fc.getExternalContext().getSession(false) != null);
        return res;
    }

    public static void redirect(String pagePath) throws IOException {
        if (!pagePath.endsWith(".xhtml")) {
            pagePath += ".xhtml";
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(pagePath);

    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.
                getCurrentInstance().
                getExternalContext().getRequest();
    }

    public static void redirectToPage(String page) {
        try {
            SessionUtil.redirect(page);
        } catch (IOException ex) {
            Logger.getLogger(SessionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
