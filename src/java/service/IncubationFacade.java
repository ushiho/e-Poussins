/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Couvoir;
import bean.Incubation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.SearchUtil;

/**
 *
 * @author lotfi
 */
@Stateless
public class IncubationFacade extends AbstractFacade<Incubation> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @EJB
    private EclosionFacade eclosionFacade;
    @EJB
    private TrieOeufFacade trieOeufFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IncubationFacade() {
        super(Incubation.class);
    }

    public List<Incubation> findByCouvoir(Couvoir couvoir) {
        if (couvoir == null || couvoir.getId() == null) {
            return null;
        }
        return getMultipleResult("SELECT i FROM Incubation i WHERE i.couvoir.id = '" + couvoir.getId() + "' ");
    }

    public BigDecimal sumOfQteIncubeByCouvoir(Couvoir couvoir) {
        if (couvoir == null || couvoir.getId() == null) {
            return null;
        }
        return calculSumBigDecimal("Incubation", "i", "qteIncube", " WHERE i.couvoir.id = '" + couvoir.getId() + "' ");
    }

    public int save(Incubation incubation) {
        if (incubation == null) {
            return -1;
        }
        create(incubation);
        eclosionFacade.create(incubation.getEclosion());
        return 1;
    }

    public Incubation findByDate(Date date) {
        return date != null ? getUniqueResult("SELECT i FROM Incubation i WHERE i.dateIncubation = '" + date + "' ") : null;
    }

    public Incubation clone(Incubation incubation) {
        if (incubation == null) {
            return null;
        }
        Incubation clone = new Incubation(incubation.getId(), incubation.getDateIncubation(), incubation.getNumeroSemaine(), incubation.getAnnee(), incubation.getQteIncube());
        clone.setCouvoir(incubation.getCouvoir());
        clone.setEclosion(incubation.getEclosion());
        clone.setTrieOeuf(incubation.getTrieOeuf());
        return clone;
    }

    public List<Incubation> findByDateMinAndMax(Date dateMax, Date dateMin) {
        return getMultipleResult("SELECT i FROM Incubation i WHERE 1=1 " + SearchUtil.addConstraintMinMax("i", "dateIncubation", dateMin, dateMax));
    }
}
