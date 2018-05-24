package controller;

import bean.CategorieOeuf;
import bean.TrieOeuf;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import java.io.IOException;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpServletRequest;
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
    private CategorieOeuf categorieOeufSelected;

    public CategorieOeuf getCategorieOeufSelected() {
        if (categorieOeufSelected == null) {
            categorieOeufSelected = new CategorieOeuf();
        }
        return categorieOeufSelected;
    }

    public void setCategorieOeufSelected(CategorieOeuf categorieOeufSelected) {
        this.categorieOeufSelected = categorieOeufSelected;
    }

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
        remplirSelected();
        getItems().add(ejbFacade.clone(selected));
        setRestReception(getRestReception().subtract(selected.getEntree()));
        setTotalEntres(getTotalEntres().add(selected.getEntree()));
        MessageUtil.info("La Catégorie '" + selected.getCategorieOeuf().getDesignation() + "' est ajoutée");
        setSelected(null);
        setCategorieOeufSelected(null);
    }

    public void addOrModifyAndTestRestReception() {
        if (getSelectedToModify().getCategorieOeuf().getId() == null) {
            if (testSFToSave()) {
                return;
            }
            addToList();
            setForme3(false);
        } else {
            System.out.println("avant lite est : " + getItems());
            System.out.println("apres ha selecet : " + selected);
            System.out.println("apres ha selecet ToModify : " + selectedToModify);

            if (testSFToSave()) {
                System.out.println("testSFToSave is true !!");
                System.out.println("apres lite est : " + getItems());
                System.out.println("apres ha selecet : " + selected);
                System.out.println("apres ha selecet ToModify : " + selectedToModify);
                return;
            }
            modifyFromList();
            setForme3(false);
        }
    }

    public TrieOeuf clone(TrieOeuf trieOeuf) {
        return ejbFacade.clone(trieOeuf);
    }

    private boolean testSFToSave() {
        System.out.println("ha liste from testSFToSave : " + getItems());
        System.out.println("avant ha selecet : " + selected);
        System.out.println("avant ha selecet ToModify : " + selectedToModify);
        if (testRestWithEntreeAndShowMsg()) {
            System.out.println("testRestAndShowMsg is true nini");
            return true;
        }
        System.out.println("ha liste from testSFToSave after testRestWithEntreeAndShowMsg: " + getItems());

        System.out.println("avant calcul sf ha selecet : " + selected);
        System.out.println("avant avant calcul sf ha selecet ToModify : " + selectedToModify);
        System.out.println("ha liste from testSFToSave before calcul sf : " + getItems());

        ejbFacade.calculateSF(selected);
        System.out.println("avant sf <0 =>ha selected : " + selected);
        System.out.println("avant sf <0 => ha selected to modify: " + selectedToModify);
        System.out.println("ha liste from testSFToSave after calcul sf : " + getItems());

        if (getSelected().getSituationFinale().compareTo(new BigDecimal(0)) < 0) {
            MessageUtil.fatal("La situation finale est innaccèptable");
            System.out.println("sf is <0 ");
            setForme3(true);
            System.out.println("sf <0 =>ha selected : " + selected);
            System.out.println("sf <0 => ha selected to modify: " + selectedToModify);
            System.out.println("avant ha liset in testSFToSave " + getItems());
            setSelected(getSelectedToModify());
            System.out.println("apres ha liset in testSFToSave " + getItems());
            return true;
        }
        return false;
    }

    private boolean testRestWithEntreeAndShowMsg() {
        if (selected.getEntree().compareTo(restReception) > 0) {
            System.out.println("in testRestWithEntreeAndShowMsg ha liste apres : " + getItems());
            MessageUtil.fatal("Le nombre en entrée est supérieur au rest de la recéption");
            return true;
        }
        return false;
    }

    public void restoreRestReception() {
        setRestReception(getRestReception().add(getSelected().getEntree()));
        setSelectedToModify(clone(selected));
        setCategorieOeufSelected(getSelected().getCategorieOeuf());
    }

    public void modifyFromList() {
        System.out.println("cc mn modify");
        setRestReception(getRestReception().subtract(selected.getEntree()));
        setTotalEntres(getTotalEntres().subtract(selectedToModify.getEntree()));
        setTotalEntres(getTotalEntres().add(selected.getEntree()));
        selected.setCategorieOeuf(categorieOeufSelected);
        MessageUtil.info("La catégorie '" + categorieOeufSelected.getDesignation() + "' est bien modifiée");
        setSelected(null);
        setSelectedToModify(null);
        setCategorieOeufSelected(null);
    }

    public void reloadAllThePage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void removeFromList() {
        getItems().remove(ejbFacade.indexOfSelected(items, selected));
        setRestReception(getRestReception().add(selected.getEntree()));
        setTotalEntres(getTotalEntres().subtract(selected.getEntree()));
        MessageUtil.info("La catégorie '" + getSelected().getCategorieOeuf().getDesignation() + "' est supprimée");
        setCategorieOeufSelected(null);
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
        TrieOeuf lastTrieOeuf = ejbFacade.getLastSavedByDay(DateUtil.getSqlDateToSaveInDB(dateTrie), categorieOeufSelected);
        if (lastTrieOeuf == null || lastTrieOeuf.getSituationFinale() == null) {
            getSelected().setSituationInitiale(new BigDecimal(0));
        } else {
            getSelected().setSituationInitiale(lastTrieOeuf.getSituationFinale());
        }
    }

    public void remplirSelected() {
        selected.setNumSemaine(semaine);
        selected.setDateTrie(DateUtil.getSqlDateToSaveInDB(dateTrie));
        selected.setReception(reception);
        selected.setCategorieOeuf(categorieOeufSelected);
    }

    public String formatDateToString(Date dateToFormat) {
        return DateUtil.formateDate("dd/MM/yyyy", dateToFormat);
    }

    public void saveItemsInDB() {
        if (items != null && !items.isEmpty() && items.get(0) != null) {
            for (TrieOeuf item : items) {
                item.setIncubations(null);
//                item.setFerme(utilisateurFacade.getConnectedUser("user").getFerme());
//                item.setResponsable(utilisateurFacade.getConnectedUser("user"));
                item.setResponsable(null);
                item.setFerme(null);
                ejbFacade.create(item);
            }
            MessageUtil.info("Vos donnés sont bien enregistrés ");
            items.clear();
            initAllParams();
            return;
        }
        MessageUtil.error("Un erreur est survenue, Essayer plus tard");
    }

    public void cancelSavingInDb() {
        getItems().clear();
        initAllParams();
    }

    private void initAllParams() {
        setSelected(null);
        setSelectedToModify(null);
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

    public void cancelModify() {
        ejbFacade.initBigDecimalsBy0(getSelectedToModify());
        setRestReception(getReception().subtract(getSelectedToModify().getEntree()));
        setSelectedToModify(null);
        setSelected(null);
        setForme3(false);
        setCategorieOeufSelected(null);
    }
}
