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
    private boolean forme4;
    private List<CategorieOeuf> categorieOeufsAdded;
    //attributes for suivis template
    private Integer inputMin;
    private Integer inputMax;
    private Integer mois;//to construct a date
    private Integer year;//to construct a date

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getInputMin() {
        return inputMin;
    }

    public void setInputMin(Integer inputMin) {
        this.inputMin = inputMin;
    }

    public Integer getInputMax() {
        return inputMax;
    }

    public void setInputMax(Integer inputMax) {
        this.inputMax = inputMax;
    }

    public List<CategorieOeuf> getCategorieOeufsAdded() {
        if (categorieOeufsAdded == null) {
            categorieOeufsAdded = new ArrayList();
        }
        return categorieOeufsAdded;
    }

    public void setCategorieOeufsAdded(List<CategorieOeuf> categorieOeufsAdded) {
        this.categorieOeufsAdded = categorieOeufsAdded;
    }

    public boolean isForme4() {
        return forme4;
    }

    public void setForme4(boolean forme4) {
        this.forme4 = forme4;
    }

    public CategorieOeuf getCategorieOeufSelected() {
        if (categorieOeufSelected == null) {
            categorieOeufSelected = new CategorieOeuf();
        }
        return categorieOeufSelected;
    }

    public void setCategorieOeufSelected(CategorieOeuf categorieOeufSelected) {
        this.categorieOeufSelected = categorieOeufSelected;
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
        if (categorieOeufFacade.categorieOeufsExiste(getCategorieOeufsAdded(), getCategorieOeufSelected())) {
            MessageUtil.showMsg("Cette catégorie est déja ajoutée");
            initSelectdAndCategorie();
            return;
        }
        getItems().add(ejbFacade.clone(selected));
        getCategorieOeufsAdded().add(categorieOeufFacade.clone(categorieOeufSelected));
        setRestReception(getRestReception().subtract(selected.getEntree()));
        setTotalEntres(getTotalEntres().add(selected.getEntree()));
        MessageUtil.info("La Catégorie '" + selected.getCategorieOeuf().getDesignation() + "' est ajoutée");
        initSelectdAndCategorie();
    }

    private void initSelectdAndCategorie() {
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
            if (testSFToSave()) {
                int index = ejbFacade.indexOfSelected(items, selected);
                if (index > 0) {
                    getItems().remove(index);
                    getItems().add(ejbFacade.clone(selectedToModify));
                }
                return;
            }
            modifyFromList();
            setForme3(false);
        }
    }

    private boolean testSFToSave() {
        if (testRestWithEntreeAndShowMsg()) {
            return true;
        }
        BigDecimal sf = ejbFacade.calculateSF(selected);
        if (sf.compareTo(new BigDecimal(0)) < 0) {
            MessageUtil.fatal("La situation finale est innaccèptable");
            setForme3(true);
            return true;
        }
        selected.setSituationFinale(sf);
        return false;
    }

    private boolean testRestWithEntreeAndShowMsg() {
        if (selected.getEntree().compareTo(restReception) > 0) {
            MessageUtil.fatal("Le nombre en entrée est supérieur au rest de la recéption");
            return true;
        }
        return false;
    }

    public void restoreRestReception() {
        setRestReception(getRestReception().add(getSelected().getEntree()));
        setSelectedToModify(ejbFacade.clone(selected));
        setCategorieOeufSelected(getSelected().getCategorieOeuf());
    }

    public void modifyFromList() {
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
        if (getRestReception().compareTo(new BigDecimal(0)) == 0) {
            ejbFacade.saveListToDB(items);
            MessageUtil.info("Vos donnés sont bien enregistrés ");
            setTotalEntres(null);
            items.clear();
            initAllParams();
            setForme4(true);
            return;
        }
        MessageUtil.addMessage("Il vous reste " + getRestReception() + " oeufs ,Choisie une catégorie"
                + "pour les suvergarder .");
    }

    public void cancelSavingInDb() {
        getItems().clear();
        setForme1(true);
        initAllParams();
    }

    public void initAllParams() {
        setSelectedToModify(null);
        setReception(null);
        setRestReception(null);
        setTotalEntres(null);
        setDateTrie("");
        setForme2(false);
        setForme3(false);
        setSemaine(null);
        initSelectdAndCategorie();
        setCategorieOeufsAdded(null);
        setItems(null);
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
        System.out.println("in cancel modify");
        if (getSelectedToModify().getCategorieOeuf().getId() != null) {
            System.out.println("cancel modify");
            ejbFacade.initBigDecimalsBy0(getSelectedToModify());
            setRestReception(getRestReception().subtract(getSelectedToModify().getEntree()));
        }
        setSelectedToModify(null);
        setForme3(false);
        initSelectdAndCategorie();
    }

    public void imprimer() {

    }

    // methods for suivis Templates
    public List<Integer> chargeYearsSavedInDB() {
        System.out.println("ha list des years : " + ejbFacade.yearsBetweenTwoDate(ejbFacade.minOrMaxDateExisteInDB(2), ejbFacade.minOrMaxDateExisteInDB(1)));
        return ejbFacade.yearsBetweenTwoDate(ejbFacade.minOrMaxDateExisteInDB(2), ejbFacade.minOrMaxDateExisteInDB(1));
    }

    public void searchJournalier() {
        System.out.println("salam c journalier");
        setItems(ejbFacade.searchJournalier(year, mois, inputMin, inputMax, categorieOeufSelected));
        System.out.println("ha list : " + getItems());
    }

}
