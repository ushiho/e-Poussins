/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ProductionPoussin;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lotfi
 */
@Stateless
public class ProductionPoussinFacade extends AbstractFacade<ProductionPoussin> {

    @PersistenceContext(unitName = "e-PoussinsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductionPoussinFacade() {
        super(ProductionPoussin.class);
    }
    
}
