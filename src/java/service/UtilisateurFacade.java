/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Utilisateur;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import util.SessionUtil;

/**
 *
 * @author lotfi
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

    public Utilisateur getConnectedUser(String cle) {
        Utilisateur connected = (Utilisateur) SessionUtil.getAttribute(cle);
        return connected;
    }

    public void logout() {
        HttpSession session = SessionUtil.getSession();
        session.invalidate();
    }

    public Utilisateur clone(Utilisateur utilisateur) {
        Utilisateur clone = new Utilisateur(utilisateur.getId(), utilisateur.getLogin(), utilisateur.getPassword(),
                utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getTel(),
                utilisateur.isBlocked(), utilisateur.getNbrEssaye(), utilisateur.isMdpChanged(), utilisateur.isAdminSys());
        clone.setQuestion(utilisateur.getQuestion());
        clone.setFerme(utilisateur.getFerme());
        return clone;
    }

    public Utilisateur findByLogin(String login) {
        return getUniqueResult("SELECT u FROM Utilisateur u WHERE u.login like '" + login + "'");
    }
}
