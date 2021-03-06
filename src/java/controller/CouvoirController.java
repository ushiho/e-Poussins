package controller;

import bean.Couvoir;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.CouvoirFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.EclosionFacade;
import service.IncubationFacade;
import util.MessageUtil;

@Named("couvoirController")
@SessionScoped
public class CouvoirController implements Serializable {

    @EJB
    private service.CouvoirFacade ejbFacade;
    @EJB
    private IncubationFacade incubationFacade;
    @EJB
    private EclosionFacade eclosionFacade;
    private List<Couvoir> items = null;
    private Couvoir selected;

    public CouvoirController() {
    }

    public Couvoir getSelected() {
        System.out.println("cc is getSelected from covoirController " + selected);
        if (selected == null) {
            selected = new Couvoir();
        }
        return selected;
    }

    public void setSelected(Couvoir selected) {
        this.selected = selected;
    }

    public List<Couvoir> getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    public void setItems(List<Couvoir> items) {
        this.items = items;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CouvoirFacade getFacade() {
        return ejbFacade;
    }

    public Couvoir prepareCreate() {
        selected = new Couvoir();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CouvoirCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CouvoirUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CouvoirDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Couvoir getCouvoir(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Couvoir> getItemsAvailableSelectMany() {
        return ejbFacade.findAll();
    }

    public List<Couvoir> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Couvoir.class)
    public static class CouvoirControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CouvoirController controller = (CouvoirController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "couvoirController");
            return controller.getCouvoir(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Couvoir) {
                Couvoir o = (Couvoir) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Couvoir.class.getName()});
                return null;
            }
        }

    }

    public BigDecimal showRestCapaciteOfCouvoir(Couvoir selected) {
        System.out.println("hi is showRestCapaciteOfCouvoir ha selected : " + selected);
        BigDecimal rest = ejbFacade.calcRestOfCapacite(selected);
        System.out.println("cc is showRestCapaciteOfCouvoir ha rest : " + rest);
        return rest;
    }

    private BigDecimal initNullvalue(BigDecimal rest) {
        if (rest == null) {
            rest = new BigDecimal(0);
        }
        return rest;
    }

    public BigDecimal showTotalIncubes(Couvoir selected) {
        BigDecimal totalIncubes = incubationFacade.sumOfQteIncubeByCouvoir(selected);
        return initNullvalue(totalIncubes);
    }

    public BigDecimal showTotalEclos(Couvoir selected) {
        BigDecimal totalEclos = eclosionFacade.calcTotalEclosByCouvoir(selected);
        return initNullvalue(totalEclos);
    }

}
