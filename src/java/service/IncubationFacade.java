/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Couvoir;
import bean.Incubation;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class IncubationFacade extends AbstractFacade<Incubation> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

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
}
