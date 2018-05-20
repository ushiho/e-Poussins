package controller;

import bean.CategorieOeuf;
import bean.TrieOeuf;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.TrieOeufFacade;

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
import service.CategorieOeufFacade;
import util.DateUtil;

@Named("trieOeufController")
@SessionScoped
public class TrieOeufController implements Serializable {

    @EJB
    private service.TrieOeufFacade ejbFacade;
    @EJB
    private CategorieOeufFacade categorieOeufFacade;
    private List<TrieOeuf> items;
    private TrieOeuf selected;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private String dateTrie;
    private BigDecimal restReception;
    private Long idCategorieOeufsChosen;

    public Long getIdCategorieOeufsChosen() {
        return idCategorieOeufsChosen;
    }

    public void setIdCategorieOeufsChosen(Long idCategorieOeufsChosen) {
        this.idCategorieOeufsChosen = idCategorieOeufsChosen;
    }

    public BigDecimal getRestReception() {
        if (restReception == null) {
            restReception = selected.getReception();
        }
        return restReception;
    }

    public void setRestReception(BigDecimal restReception) {
        this.restReception = restReception;
    }

    public String getDateTrie() {
        return dateTrie;
    }

    public void setDateTrie(String dateTrie) {
        this.dateTrie = dateTrie;
    }

    public TrieOeufController() {
    }

    public TrieOeuf getSelected() {
        if (selected == null) {
            selected = new TrieOeuf();
        }
        return selected;
    }

    public void setSelected(TrieOeuf selected) {
        this.selected = selected;
    }

    public TrieOeufFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(TrieOeufFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public List<TrieOeuf> getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    public void setItems(List<TrieOeuf> items) {
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

    private TrieOeufFacade getFacade() {
        return ejbFacade;
    }

    public TrieOeuf prepareCreate() {
        selected = new TrieOeuf();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TrieOeufCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TrieOeufUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TrieOeufDeleted"));
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

    public TrieOeuf getTrieOeuf(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TrieOeuf> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TrieOeuf> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = TrieOeuf.class)
    public static class TrieOeufControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrieOeufController controller = (TrieOeufController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trieOeufController");
            return controller.getTrieOeuf(getKey(value));
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
            if (object instanceof TrieOeuf) {
                TrieOeuf o = (TrieOeuf) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TrieOeuf.class.getName()});
                return null;
            }
        }

    }

    public void addToList() {
        if (selected != null) {
            getItems().add(ejbFacade.clone(selected));
        }

    }

//    public void modifyFromList() {
//        System.out.println("dkhl l modify : ");
//        if (selected == null) {
//            MessageUtil.showMsgSelectToModify();
//        } else {
//            getItems().remove(selected);
//            setDates(DateUtil.formateDate("dd/MM/YYYY", selected.getDateDebut()),
//                    DateUtil.formateDate("dd/MM/YYYY", selected.getDateFin()));
//            setShowForm(true);
//        }
//    }
//
//    public void removeFromList() {
//        if (selected == null) {
//            MessageUtil.showMsgSelectToRemove();
//        } else {
//            ejbFacade.removeSelectedFromList(getItems(), selected);
//            setShowForm(false);
//            System.out.println("ha size : " + getItems().size());
//            System.out.println("ha liste => : " + items);
//            calculTotalMontants(2, selected);
//            selected = null;
//        }
//    }
    public void showForme3ByCategorie() {
        if (selected != null && selected.getCategorieOeuf() != null && selected.getCategorieOeuf().getId() != null) {
            setForme3(true);
            return;
        }
        setForme3(false);
    }

    public void setSIToTheSelected() {
        TrieOeuf lasteTrieOeuf = ejbFacade.getLastTrieSavedByDate(selected);
        if (lasteTrieOeuf == null || lasteTrieOeuf.getSituationFinale() == null) {
            selected.setSituationInitiale(new BigDecimal(0));
        } else {
            selected.setSituationInitiale(lasteTrieOeuf.getSituationFinale());
        }
    }

    public void setDateToTheSelected() {
        selected.setDateTrie(DateUtil.getSqlDateToSaveInDB(dateTrie));
    }
}
