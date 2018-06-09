/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Couvoir;
import bean.Eclosion;
import bean.Incubation;
import bean.TrieOeuf;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class EclosionFacade extends AbstractFacade<Eclosion> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @EJB
    private TrieOeufFacade trieOeufFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EclosionFacade() {
        super(Eclosion.class);
    }

    public Eclosion findByIncubation(Incubation incubation) {
        if (incubation == null || incubation.getId() == null) {
            return null;
        }
        return getUniqueResult("SELECT e FROM Eclosion e WHERE e.incubation.id = '" + incubation.getId() + "' ");
    }

    public BigDecimal calcEcartEclos(Eclosion eclosion) {
        if (eclosion == null || eclosion.getIncubation() == null) {
            return null;
        }
        return ((eclosion.getIncubation().getQteIncube()).subtract(eclosion.getQteEclos()));
    }

    public BigDecimal calcTotalEclosByCouvoir(Couvoir couvoir) {
        if (couvoir == null || couvoir.getId() == null) {
            return null;
        }
        return calculSumBigDecimal("Eclosion", "eclo", "qteEclos", " WHERE eclo.incubation.couvoir.id = '" + couvoir.getId() + "' ");
    }

    public TrieOeuf findTrieOeufByCriteria(Date dateCriteria, int typeDate) {
        String req = "SELECT tr FROM TrieOeuf tr WHERE ";
        if (dateCriteria != null) {
            return trieOeufFacade.getUniqueResult(req + completeQuery(typeDate, "tr"));
        }
        return null;
    }

    private String completeQuery(int typeDate, String beanAbrev) {
        switch (typeDate) {
            case 1:
                return beanAbrev + ".dateTrie ";
            case 2:
                return beanAbrev + ".incubation.dateIncubation ";
            case 3:
                return beanAbrev + ".incubation.eclosion.dateEclosion ";
            default:
                return " 1!=1";
        }
    }

    public int save(Eclosion eclosion) {
        if (eclosion == null || eclosion.getId() == null) {
            return -1;
        }
        edit(eclosion);
        return 1;
    }

    public BigDecimal ecartTrie(Eclosion eclosion) {
        if (eclosion == null || eclosion.getQteEclos() == null) {
            return null;
        }
        return eclosion.getQteEclos().subtract(eclosion.getCommercialise());
    }

    public BigDecimal ecartEclos(Eclosion eclosion) {
        if (eclosion == null || eclosion.getIncubation().getQteIncube() == null) {
            return null;
        }
        return eclosion.getIncubation().getQteIncube().subtract(eclosion.getQteEclos());
    }
}
