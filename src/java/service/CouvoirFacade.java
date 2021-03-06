/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Couvoir;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class CouvoirFacade extends AbstractFacade<Couvoir> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @EJB
    private IncubationFacade incubationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CouvoirFacade() {
        super(Couvoir.class);
    }

    public BigDecimal calcRestOfCapacite(Couvoir couvoir) {
        if (couvoir != null) {
            BigDecimal qteIncubes = incubationFacade.sumOfQteIncubeByCouvoir(couvoir);
            System.out.println("hi from calcRestOfCapacite ha couvoir=> " + couvoir);
            if (qteIncubes == null) {
                return couvoir.getCapacite();
            }
            return couvoir.getCapacite().subtract(qteIncubes);
        }
        return null;
    }

}
