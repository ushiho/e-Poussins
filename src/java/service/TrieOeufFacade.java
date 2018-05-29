/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CategorieOeuf;
import bean.TrieOeuf;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB
    private CategorieOeufFacade categorieOeufFacade;

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

    public BigDecimal calculateSF(TrieOeuf trieOeuf) {
        if (trieOeuf == null) {
            return null;
        }
        initBigDecimalsBy0(trieOeuf);
        return (trieOeuf.getSituationInitiale().add(trieOeuf.getEntree()))
                .subtract((trieOeuf.getMisEnIncubation().add(trieOeuf.getPerte()).add(
                        trieOeuf.getVente()).add(trieOeuf.getDon())));
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

    public int indexOfSelected(List<TrieOeuf> trieOeufs, TrieOeuf selected) {
        if (testItems(trieOeufs)) {
            return -1;
        }
        for (int i = 0; i < trieOeufs.size(); i++) {
            TrieOeuf item = trieOeufs.get(i);
            if (equals(item, selected)) {
                return i;
            }
        }
        return -1;
    }

    public boolean testItems(List<TrieOeuf> trieOeufs) {
        return trieOeufs == null || trieOeufs.isEmpty() || trieOeufs.get(0) == null;
    }

    public boolean equals(TrieOeuf item, TrieOeuf selected) {
        return (item.getCategorieOeuf().equals(selected.getCategorieOeuf()) && item.getDateTrie().equals(selected.getDateTrie())
                && item.getDon().compareTo(selected.getDon()) == 0 && item.getEntree().compareTo(selected.getEntree()) == 0
                && item.getMisEnIncubation().equals(selected.getMisEnIncubation()) && item.getNumSemaine().equals(selected.getNumSemaine())
                && item.getPerte().equals(selected.getPerte()) && item.getReception().equals(selected.getReception())
                && item.getSituationFinale().equals(selected.getSituationFinale()) && item.getSituationInitiale().equals(selected.getSituationInitiale())
                && item.getVente().equals(selected.getVente()));
    }

    //add responsable in params to save it and save femre also
    public void saveListToDB(List<TrieOeuf> items) {
        System.out.println("hi from saveListToDB ha liste : " + items);
        if (!testItems(items)) {
            for (TrieOeuf item : items) {
                item.setIncubations(null);
//                item.setFerme(utilisateurFacade.getConnectedUser("user").getFerme());
//                item.setResponsable(utilisateurFacade.getConnectedUser("user"));
                item.setResponsable(null);
                item.setFerme(null);
                create(item);
            }
        }
    }

//    public List<TrieOeuf> findByCriteria(Date dateMin, Date dateMax, CategorieOeuf categorieOeuf) {
//        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 ";
//    }
    public Date minOrMaxDateExisteInDB(int minOrMax) {
        String req = "SELECT ";
        switch (minOrMax) {
            case 1:
                req += " MAX";
                break;
            case 2:
                req += " MIN";
                break;
        }
        req += "(tr.dateTrie) FROM TrieOeuf tr";
        return (Date) em.createQuery(req).getResultList().get(0);
    }

    public List<Integer> yearsBetweenTwoDate(Date dateMin, Date dateMax) {
        List<Integer> years = new ArrayList();
        int yearMin = DateUtil.getYearOfDate(dateMin);
        int yearMax = DateUtil.getYearOfDate(dateMax);
        System.out.println("year max  " + yearMax);
        System.out.println("year min " + yearMin);
        fillListOfYears(yearMax, yearMin, years);
        return years;
    }

    private void fillListOfYears(int yearMax, int yearMin, List<Integer> years) {
        if (yearMax == yearMin) {
            years.add(yearMax);
        } else {
            for (int i = yearMin; i < yearMax; i++) {
                years.add(i);
            }
        }
    }

    public List<TrieOeuf> searchByDateMinOrMaxOrCategorie(Date dateMin, Date dateMax, CategorieOeuf categorieOeuf) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 ";
        if (dateMax != null) {
            req += " AND tr.dateTrie <= '" + dateMax + "' ";
        }
        if (dateMax != null) {
            req += " AND tr.dateTrie >= '" + dateMin + "' ";
        }
        if (categorieOeuf != null) {
            req += " AND tr.categorieOeuf.id = '" + categorieOeuf.getId() + "'";
        }
        System.out.println("cc from seacrh requete ha dateMin : " + dateMin);
        System.out.println("cc from seacrh requete ha dateMax : " + dateMax);
        return getMultipleResult(req);
    }

    public List<TrieOeuf> searchJournalier(Integer year, Integer month, Integer dayMin, Integer dayMax, CategorieOeuf categorieOeuf) {
        System.out.println("searchJournalier ha day Max in entree : " + dayMax);
        if (year == null || month == null) {
            return null;
        } else {
            if (dayMin == null) {
                dayMin = 01;
            }
            if (dayMax == null) {
                System.out.println("cc dayMax is null o ha res d fct: " + DateUtil.maxDayInMonthOfYear(year, month));
                dayMax = DateUtil.maxDayInMonthOfYear(year, month);
            }
            System.out.println("from sevice searchJournalier ha day max " + dayMax);
            String moisAndYear = month + "/" + year;
            System.out.println("service => from searchJournalier ha date min :" + DateUtil.getSqlDateToSaveInDB(dayMin + "/" + moisAndYear));
            return searchByDateMinOrMaxOrCategorie(DateUtil.getSqlDateToSaveInDB(dayMin + "/" + moisAndYear),
                    DateUtil.getSqlDateToSaveInDB(dayMax + "/" + moisAndYear), categorieOeuf);
        }
    }

    public List<TrieOeuf> findByNumSemaine(Integer year, Integer semaineMax, Integer semaineMin, CategorieOeuf categorieOeuf) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 ";
        if (semaineMax != null) {
            req += " AND tr.numSemaine >= '" + semaineMax + "' ";
        }
        if (semaineMin != null) {
            req += " AND tr.numSemaine <= '" + semaineMin + "' ";
        }
        if (year != null) {
            // select the year
        }
        if (categorieOeuf != null) {
            req += " AND tr.categorieOeuf.id = '" + categorieOeuf.getId() + "' ";
        }
        return getMultipleResult(req);
    }

}
