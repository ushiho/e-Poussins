package controller;

import bean.TrieOeuf;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.TrieOeufFacade;

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
import service.UtilisateurFacade;
import util.DateUtil;
import util.MessageUtil;

@Named("trieOeufController")
@SessionScoped
public class TrieOeufController implements Serializable {

    @EJB
    private service.TrieOeufFacade ejbFacade;
    @EJB
    private CategorieOeufFacade categorieOeufFacade;
    @EJB
    private UtilisateurFacade utilisateurFacade;
    private List<TrieOeuf> items;
    private TrieOeuf selected;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private String dateTrie;
    private BigDecimal restReception;
    private BigDecimal totalEntres = new BigDecimal(0);
    private BigDecimal reception;
    private Integer semaine;
    private TrieOeuf selectedToModify;

    public UtilisateurFacade getUtilisateurFacade() {
        return utilisateurFacade;
    }

    public void setUtilisateurFacade(UtilisateurFacade utilisateurFacade) {
        this.utilisateurFacade = utilisateurFacade;
    }

    public TrieOeuf getSelectedToModify() {
        if (selectedToModify == null) {
            selectedToModify = new TrieOeuf();
        }
        return selectedToModify;
    }

    public void setSelectedToModify(TrieOeuf selectedToModify) {
        this.selectedToModify = selectedToModify;
    }

    public BigDecimal getTotalEntres() {
        return totalEntres;
    }

    public void setTotalEntres(BigDecimal totalEntres) {
        this.totalEntres = totalEntres;
    }

    public BigDecimal getReception() {
        return reception;
    }

    public void setReception(BigDecimal reception) {
        this.reception = reception;
    }

    public CategorieOeufFacade getCategorieOeufFacade() {
        return categorieOeufFacade;
    }

    public void setCategorieOeufFacade(CategorieOeufFacade categorieOeufFacade) {
        this.categorieOeufFacade = categorieOeufFacade;
    }

    public Integer getSemaine() {
        return semaine;
    }

    public void setSemaine(Integer semaine) {
        this.semaine = semaine;
    }

    public BigDecimal getRestReception() {
        if (restReception == null) {
            restReception = getReception();
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
        if (selected != null && !dateTrie.equals("")) {
            setDateAndSemainToTheSelected();
            ejbFacade.calculateSF(selected);
            getItems().add(ejbFacade.clone(selected));
            setRestReception(getRestReception().subtract(selected.getEntree()));
            setTotalEntres(getTotalEntres().add(selected.getEntree()));
            MessageUtil.info("La Catégorie '" + selected.getCategorieOeuf().getDesignation() + "' est ajoutée");
            setSelected(null);
        }

    }

    public void restoreRestReception() {
        if (getRestReception().compareTo(getReception()) < 0) {
            setRestReception(getRestReception().add(selected.getEntree()));
        }
        setSelectedToModify(ejbFacade.clone(selected));
    }

    public void modifyFromList() {
        ejbFacade.calculateSF(selected);
        System.out.println("modify => ha selected : " + selected);
        System.out.println("modify => index of selected to modify: " + getItems().indexOf(selectedToModify));
        System.out.println("modify => o ha selected to modify: " + selectedToModify);
        System.out.println("avant ha la liste : " + getItems());
        setRestReception(getRestReception().subtract(selected.getEntree()));
        setTotalEntres(getTotalEntres().subtract(selectedToModify.getEntree()));
        setTotalEntres(getTotalEntres().add(selected.getEntree()));
        System.out.println("apres ha la liste : " + getItems());
        MessageUtil.info("Cette catégorie est bien modifié");
    }

    public void removeFromList() {
        getItems().remove(selected);
        setRestReception(getRestReception().add(selected.getEntree()));
        setTotalEntres(getTotalEntres().subtract(selected.getEntree()));
        MessageUtil.info("Cette catégorie est supprimée");
        setSelected(null);
    }

    public void showForme3ByCategorie() {
        if (selected != null && selected.getCategorieOeuf() != null && selected.getCategorieOeuf().getId() != null) {
            setForme3(true);
            return;
        }
        setForme3(false);
    }

    public void setSIToTheSelected() {
        System.out.println("hi ,from si to th selected : ha forme3= "+isForme3());
        TrieOeuf lasteTrieOeuf = ejbFacade.getLastTrieSavedByDay(selected);
        if (lasteTrieOeuf == null || lasteTrieOeuf.getSituationFinale() == null) {
            selected.setSituationInitiale(new BigDecimal(0));
        } else {
            selected.setSituationInitiale(lasteTrieOeuf.getSituationFinale());
        }
    }

    public void setDateAndSemainToTheSelected() {
        selected.setNumSemaine(semaine);
        selected.setDateTrie(DateUtil.getSqlDateToSaveInDB(dateTrie));
        selected.setReception(reception);
    }

    public String formatDateToString(Date dateToFormat) {
        return DateUtil.formateDate("dd/MM/yyyy", dateToFormat);
    }

    public void saveItemsInDB() {
        System.out.println("hi i m here");
        if (items != null && !items.isEmpty() && items.get(0) != null) {
            for (TrieOeuf item : items) {
                item.setIncubations(null);
//                item.setFerme(utilisateurFacade.getConnectedUser("user").getFerme());
//                item.setResponsable(utilisateurFacade.getConnectedUser("user"));
                item.setResponsable(null);
                item.setFerme(null);
                ejbFacade.create(item);
            }
//            return true;
            MessageUtil.info("Vos donnés sont bien enregistrés ");
            items.clear();
            initAllParams();
            return;
        }
        MessageUtil.error("Un erreur est survenue, Essayer plus tard");
//        return false;
    }

    public void cancelSavingInDb() {
        getItems().clear();
        initAllParams();
    }

    private void initAllParams() {
        setReception(null);
        setRestReception(null);
        setDateTrie("");
        setForme1(true);
        setForme2(false);
        setForme3(false);
        setSemaine(null);
    }

    public boolean testFields(int cas) {
        System.out.println("ha selecetd : " + selected);
        switch (cas) {
            case 1:
                if (selected.getEntree().compareTo(restReception) < 0) {
                    return false;
                }
                return true;
            case 2:
                if (restReception.compareTo(totalEntres) != 0) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
}
