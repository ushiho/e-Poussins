/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CategorieOeuf;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class CategorieOeufFacade extends AbstractFacade<CategorieOeuf> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void test() {
        System.out.println("ha text validi from test catego facad");
    }

    public CategorieOeufFacade() {
        super(CategorieOeuf.class);
    }

    public boolean categorieOeufsExiste(List<CategorieOeuf> categorieOeufs, CategorieOeuf categorieOeuf) {
        if (categorieOeuf == null) {
            System.out.println("categorie == null ou autre chose");
            return false;
        }
        return categorieOeufs.contains(categorieOeuf);
    }

    public CategorieOeuf clone(CategorieOeuf categorieOeuf) {
        if (categorieOeuf != null) {
            CategorieOeuf clone = new CategorieOeuf(categorieOeuf.getId(), categorieOeuf.getDesignation());
            return clone;
        }
        return null;
    }

    public CategorieOeuf findByNom(String nom) {
        if (nom != null && !"".equals(nom)) {
            return getUniqueResult("SELECT cat FROM CategorieOeuf cat WHERE cat.designation LIKE '" + nom + "'");
        }
        return null;
    }
}
