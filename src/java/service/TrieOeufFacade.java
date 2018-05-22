/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.TrieOeuf;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;

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
        TrieOeuf clone = new TrieOeuf(trieOeuf.getReception(), trieOeuf.getMisEnIncubation(), trieOeuf.getId(),
                trieOeuf.getDateTrie(), trieOeuf.getNumSemaine(), trieOeuf.getEntree(), trieOeuf.getVente(),
                trieOeuf.getDon(), trieOeuf.getPerte(), trieOeuf.getSituationFinale(), trieOeuf.getSituationInitiale());
        clone.setCategorieOeuf(trieOeuf.getCategorieOeuf());
        clone.setFerme(trieOeuf.getFerme());
        clone.setIncubations(trieOeuf.getIncubations());
        return clone;
    }

    public TrieOeuf getLastTrieSavedByDay(TrieOeuf trieOeuf) {
        System.out.println("f services => ha selecetd : " + trieOeuf);
        if (trieOeuf != null) {
            String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 ";
            Date dateOfLastTrie = DateUtil.getSqlDate(DateUtil.subDayFromDate(trieOeuf.getDateTrie()));
            if (dateOfLastTrie != null) {
                req += " AND tr.dateTrie = '" + dateOfLastTrie + "' AND tr.categorieOeuf.id = '" + trieOeuf.getCategorieOeuf().getId() + "'";
            }
            return getUniqueResult(req);
        }
        return null;
    }

    public void calculateSF(TrieOeuf trieOeuf) {
        if (trieOeuf == null) {
            return;
        }
        if (trieOeuf.getMisEnIncubation() == null) {
            trieOeuf.setMisEnIncubation(new BigDecimal(0));
        }
        if (trieOeuf.getEntree() == null) {
            trieOeuf.setEntree(new BigDecimal(0));
        }
        if (trieOeuf.getPerte() == null) {
            trieOeuf.setPerte(new BigDecimal(0));
        }
        if (trieOeuf.getVente() == null) {
            trieOeuf.setVente(new BigDecimal(0));
        }
        if (trieOeuf.getDon() == null) {
            trieOeuf.setDon(new BigDecimal(0));
        }
        trieOeuf.setSituationFinale((trieOeuf.getSituationInitiale().add(trieOeuf.getEntree()))
                .subtract((trieOeuf.getMisEnIncubation().add(trieOeuf.getPerte()).add(
                        trieOeuf.getVente()).add(trieOeuf.getDon()))));
    }
}
