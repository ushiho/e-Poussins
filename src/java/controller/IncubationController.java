package controller;

import bean.Incubation;
import bean.TrieOeuf;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.IncubationFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import service.CategorieOeufFacade;
import service.TrieOeufFacade;
import util.DateUtil;
import util.MessageUtil;

@Named("incubationController")
@SessionScoped
public class IncubationController implements Serializable {

    @EJB
    private service.IncubationFacade ejbFacade;
    @EJB
    private TrieOeufFacade trieOeufFacade;
    @EJB
    private CategorieOeufFacade categorieOeufFacade;
    private List<Incubation> items = null;
    private Incubation selected;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private boolean modal;
    private TrieOeuf trieOeufOAC;
    private String dateTrie;
    private boolean nextToForme2;

    public boolean isModal() {
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isNextToForme2() {
        return nextToForme2;
    }

    public void setNextToForme2(boolean nextToForme2) {
        this.nextToForme2 = nextToForme2;
    }

    public String getDateTrie() {
        if (dateTrie == null) {
            setDateTrie("");
        }
        return dateTrie;
    }

    public void setDateTrie(String dateTrie) {
        this.dateTrie = dateTrie;
    }

    public TrieOeuf getTrieOeufOAC() {
        return trieOeufOAC;
    }

    public void setTrieOeufOAC(TrieOeuf trieOeufOAC) {
        this.trieOeufOAC = trieOeufOAC;
    }

    public IncubationController() {
    }

    public Incubation getSelected() {
        if (selected == null) {
            selected = new Incubation();
        }
        return selected;
    }

    public void setSelected(Incubation selected) {
        this.selected = selected;
    }

    public IncubationFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(IncubationFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<Incubation> getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    public void setItems(List<Incubation> items) {
        this.items = items;
    }

    public boolean isForme1() {
        return forme1;
    }

    public void setForme1(boolean forme1) {
        this.forme1 = forme1;
    }

    public boolean isForme2() {
        return forme2;
    }

    public void setForme2(boolean forme2) {
        this.forme2 = forme2;
    }

    public boolean isForme3() {
        return forme3;
    }

    public void setForme3(boolean forme3) {
        this.forme3 = forme3;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private IncubationFacade getFacade() {
        return ejbFacade;
    }

    public Incubation prepareCreate() {
        selected = new Incubation();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("IncubationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("IncubationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("IncubationDeleted"));
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

    public Incubation getIncubation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Incubation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Incubation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Incubation.class)
    public static class IncubationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IncubationController controller = (IncubationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "incubationController");
            return controller.getIncubation(getKey(value));
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
            if (object instanceof Incubation) {
                Incubation o = (Incubation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Incubation.class.getName()});
                return null;
            }
        }

    }

    public void seachForTrieOeufOACByDate() {
        System.out.println("ha date : " + dateTrie);
        setTrieOeufOAC(trieOeufFacade.findOACByDate(DateUtil.getSqlDateToSaveInDB(dateTrie)));
        if (getTrieOeufOAC() == null) {
            MessageUtil.info("Pas de trie trouvé pour la date " + dateTrie + "");
        } else {
            thereIsIncubation();
        }
    }

    public void thereIsIncubation() {
        setNextToForme2(!(trieOeufOAC.getMisEnIncubation().compareTo(new BigDecimal(0)) == 0));
    }

    public void test() {
        System.out.println("c  v hama");
    }
}
