/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Couvoir;
import bean.Eclosion;
import bean.Incubation;
import java.math.BigDecimal;
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
}
