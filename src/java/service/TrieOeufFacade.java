/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.TrieOeuf;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class TrieOeufFacade extends AbstractFacade<TrieOeuf> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrieOeufFacade() {
        super(TrieOeuf.class);
    }

    public TrieOeuf clone(TrieOeuf trieOeuf) {
        if (trieOeuf == null) {
            return null;
        }
        TrieOeuf clone = new TrieOeuf(trieOeuf.getMisEnIncubation(), trieOeuf.getReception(), trieOeuf.getId(),
                trieOeuf.getDateTrie(), trieOeuf.getNumSemaine(), trieOeuf.getEntree(), trieOeuf.getVente(),
                trieOeuf.getDon(), trieOeuf.getPerte(), trieOeuf.getSituationFinale(), trieOeuf.getSituationInitiale());
        clone.setCategorieOeuf(trieOeuf.getCategorieOeuf());
        clone.setFerme(trieOeuf.getFerme());
        clone.setIncubations(trieOeuf.getIncubations());
        return clone;
    }

    public TrieOeuf getLastTrieSavedByDate(TrieOeuf trieOeuf) {
        System.out.println("f services => ha selecetd : " + trieOeuf);
        if (trieOeuf != null) {
            String req = "SELECT tr FROM TrieOeuf tr WHERE tr.dateTrie = '" + trieOeuf.getDateTrie() + "' "
                    + " AND tr.categorieOeuf.id = '" + trieOeuf.getCategorieOeuf().getId() + "'";
            return getUniqueResult(req);
        }
        return null;
    }

}
