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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;
import util.SearchUtil;

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
        clone.setIncubation(trieOeuf.getIncubation());
        return clone;
    }

    public TrieOeuf getLastSavedByDay(Date date, CategorieOeuf categorieOeuf) {
        return getUniqueResult("SELECT tr FROM TrieOeuf tr WHERE 1=1 "
                + SearchUtil.addConstraint("tr", "dateTrie", "=", DateUtil.subDayFromDate(date))
                + SearchUtil.addConstraint("tr", "categorieOeuf.id", "=", categorieOeuf.getId()));
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
                item.setIncubation(null);
//                item.setFerme(utilisateurFacade.getConnectedUser("user").getFerme());
//                item.setResponsable(utilisateurFacade.getConnectedUser("user"));
                item.setResponsable(null);
                item.setFerme(null);
                create(item);
            }
        }
    }

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
        fillListOfYears(DateUtil.getYearOfDate(dateMax), DateUtil.getYearOfDate(dateMin), years);
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

    public List<TrieOeuf> findByDateMinOrMaxOrCategorie(Date dateMin, Date dateMax, CategorieOeuf categorieOeuf) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 "
                + SearchUtil.addConstraintMinMax("tr", "dateTrie", dateMin, dateMax);
        if (categorieOeuf != null) {
            req += SearchUtil.addConstraint("tr", "categorieOeuf.id", "=", categorieOeuf.getId());
        }
        return getMultipleResult(req);
    }

    public List<TrieOeuf> searchJournalier(Integer year, Integer month, Integer dayMin, Integer dayMax, CategorieOeuf categorieOeuf) {
        if (year == null || month == null) {
            return null;
        } else {
            if (dayMin == null) {
                dayMin = 01;
            }
            if (dayMax == null) {
                dayMax = DateUtil.maxDayInMonthOfYear(year, month);
            }
            String moisAndYear = month + "/" + year;
            return findByDateMinOrMaxOrCategorie(DateUtil.getSqlDateToSaveInDB(dayMin + "/" + moisAndYear),
                    DateUtil.getSqlDateToSaveInDB(dayMax + "/" + moisAndYear), categorieOeuf);
        }
    }

    public List<TrieOeuf> findByNumSemaineOrCategorie(Integer year, Integer semaineMax, Integer semaineMin, CategorieOeuf categorieOeuf) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE 1=1 "
                + SearchUtil.addConstraintMinMax("tr", "numSemaine", semaineMin, semaineMax);
        if (categorieOeuf != null) {
            req += SearchUtil.addConstraint("tr", "categorieOeuf.id", "=", categorieOeuf.getId());
        }
        if (year != null) {

        }
        //il manque l annés !!!!!!!!!!!!!
        return getMultipleResult(req);
    }

    public TrieOeuf findOACByDate(Date date) {
        if (date != null) {
            return getUniqueResult("SELECT tr FROM TrieOeuf tr WHERE tr.dateTrie = '" + date + "'"
                    + " AND tr.categorieOeuf.designation LIKE 'OAC' ");
        }
        return null;
    }

    public int countElementExistInWeek(List<TrieOeuf> trieOeufsInDifferentDateAndReception, int semaine) {
        int i = 0;
        i = trieOeufsInDifferentDateAndReception.stream().filter((trieOeuf) -> (trieOeuf.getNumSemaine() == semaine)).map((_item) -> 1).reduce(i, Integer::sum);
        return i;
    }

    public List<TrieOeuf> triesInSameDateAndReception(List<TrieOeuf> trieOeufs, Date date, BigDecimal reception) {
        List<TrieOeuf> triesInSameDateAndReception = new ArrayList();
        for (TrieOeuf trieOeuf : trieOeufs) {
            if (trieOeuf.getDateTrie().compareTo(date) == 0 && trieOeuf.getReception().compareTo(reception) == 0) {
                triesInSameDateAndReception.add(trieOeuf);
            }
        }
        return triesInSameDateAndReception;
    }

    public void arrangeTrieByDate(List<TrieOeuf> trieOeufs) {
        System.out.println("cc from arrangeTrieByDate ha list : " + trieOeufs);
        trieOeufs.sort(Comparator.comparing(o -> o.getDateTrie()));
    }

    public BigDecimal calculTotalPerte(List<TrieOeuf> trieOeufs) {
        BigDecimal total = new BigDecimal(0);
        if (trieOeufs == null || trieOeufs.isEmpty()) {
            return total;
        }
        for (TrieOeuf trieOeuf : trieOeufs) {
            total.add(trieOeuf.getPerte());
        }
        return total;
    }

    public TrieOeuf findOACByDateInubsOrEclosOrDateTrie(Date dateTrie, Date dateIncub, Date dateEclos) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE tr.categorieOeuf.designation = 'OAC' " + SearchUtil.addConstraint("tr", "dateTrie", "=", dateTrie)
                + SearchUtil.addConstraint("tr", "incubation.dateIncubation", "=", dateIncub) + SearchUtil.addConstraint("tr", "incubation.eclosion.dateEclosion", "=", dateEclos);
        return getUniqueResult(req);
    }
}
