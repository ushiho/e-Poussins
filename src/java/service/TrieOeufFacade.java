/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CategorieOeuf;
import static bean.Incubation_.trieOeuf;
import bean.TrieOeuf;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public TrieOeuf getLastSavedByDay(Date date, CategorieOeuf categorieOeuf) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 ";
        if (date != null) {
            req += " AND tr.dateTrie = '" + DateUtil.subDayFromDate(date) + "' ";
        }
        if (categorieOeuf != null) {
            req += " AND tr.categorieOeuf.id = '" + categorieOeuf.getId() + "'";
        }
        return getUniqueResult(req);
    }

    public void calculateSF(TrieOeuf trieOeuf) {
        if (trieOeuf == null) {
            return;
        }
        initBigDecimalsBy0(trieOeuf);
        trieOeuf.setSituationFinale((trieOeuf.getSituationInitiale().add(trieOeuf.getEntree()))
                .subtract((trieOeuf.getMisEnIncubation().add(trieOeuf.getPerte()).add(
                        trieOeuf.getVente()).add(trieOeuf.getDon()))));
    }

    public void initBigDecimalsBy0(TrieOeuf trieOeuf1) {
        if (trieOeuf1.getSituationFinale() == null) {
            trieOeuf1.setSituationFinale(new BigDecimal(0));
        }
        if (trieOeuf1.getMisEnIncubation() == null) {
            trieOeuf1.setMisEnIncubation(new BigDecimal(0));
        }
        if (trieOeuf1.getEntree() == null) {
            trieOeuf1.setEntree(new BigDecimal(0));
        }
        if (trieOeuf1.getPerte() == null) {
            trieOeuf1.setPerte(new BigDecimal(0));
        }
        if (trieOeuf1.getVente() == null) {
            trieOeuf1.setVente(new BigDecimal(0));
        }
        if (trieOeuf1.getDon() == null) {
            trieOeuf1.setDon(new BigDecimal(0));
        }
    }

    public List<TrieOeuf> cloneList(List<TrieOeuf> trieOeufs) {
        if (trieOeufs == null || trieOeufs.isEmpty() || trieOeufs.get(0) == null) {
            return null;
        }
        List<TrieOeuf> clones = new ArrayList();
        trieOeufs.forEach((trieOeuf) -> {
            clones.add(clone(trieOeuf));
        });
        return clones;
    }
}
