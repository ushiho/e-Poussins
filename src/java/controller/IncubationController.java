package controller;

import bean.Couvoir;
import bean.Incubation;
import bean.TrieOeuf;
import service.IncubationFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import service.CategorieOeufFacade;
import service.CouvoirFacade;
import service.TrieOeufFacade;
import util.DateUtil;
import util.MessageUtil;
import util.SessionUtil;

@Named("incubationController")
@SessionScoped
public class IncubationController implements Serializable {

    @EJB
    private service.IncubationFacade ejbFacade;
    @EJB
    private TrieOeufFacade trieOeufFacade;
    @EJB
    private CategorieOeufFacade categorieOeufFacade;
    @EJB
    private CouvoirFacade couvoirFacade;
    private List<Incubation> items;
    private Incubation selected;
    private Incubation selectedToModify;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private boolean modal;
    private TrieOeuf trieOeufOAC;
    private String dateTrie;
    private String dateEclos;
    private String dateIncubes;
    private boolean nextToForme2;
    private BigDecimal restOfReception;

    public Incubation getSelectedToModify() {
        if (selectedToModify == null) {
            selectedToModify = new Incubation();
        }
        return selectedToModify;
    }

    public void setSelectedToModify(Incubation selectedToModify) {
        this.selectedToModify = selectedToModify;
    }

    public BigDecimal getRestOfReception() {
        return restOfReception;
    }

    public void setRestOfReception(BigDecimal restOfReception) {
        this.restOfReception = restOfReception;
    }

    public String getDateEclos() {
        return dateEclos;
    }

    public void setDateEclos(String dateEclos) {
        this.dateEclos = dateEclos;
    }

    public String getDateIncubes() {
        return dateIncubes;
    }

    public void setDateIncubes(String dateIncubes) {
        this.dateIncubes = dateIncubes;
    }

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

    public void searchForTrieOeufOACByDate() {
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

    public void fromeForm1ToForm2() {
        setForme1(false);
        setForme2(true);
        setNextToForme2(false);
        getSelected().setTrieOeuf(trieOeufOAC);
        getSelected().setQteIncube(trieOeufOAC.getMisEnIncubation());
    }

    public void fromeForm2ToForm1() {
        setForme2(false);
        setForme1(true);
        setNextToForme2(true);
        getSelected().setTrieOeuf(null);
        getSelected().setQteIncube(null);
    }

    public void validateCouvoir(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            getSelected().setCouvoir((Couvoir) value);
            if (trieOeufOAC.getMisEnIncubation().compareTo(getSelected().getCouvoir().getCapacite()) > 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL,
                        "La capacité de couvoir choisis est insuffisant ,chosis un autre couvoir pour continuer", "Pas de détail"));
            }
        }
    }

    public Long joursRestant() {
        if (dateEclos.equals("") || dateEclos == null || getSelected().getDateIncubation() == null) {
            return null;
        }
        long jrs = TimeUnit.DAYS.convert(DateUtil.getSqlDateToSaveInDB(dateEclos).getTime()
                - (new Date()).getTime(), TimeUnit.MILLISECONDS);
        return jrs < 0 ? 0 : jrs;
    }

    public void save() {
        getSelected().getEclosion().setDateEclosion(DateUtil.getSqlDateToSaveInDB(dateEclos));
        int res = ejbFacade.save(selected);
        if (res < 0) {
            MessageUtil.error("Pas d'incubation à enregistrer");
            return;
        }
        MessageUtil.info("L'incubation est enregistrée");
        initParams();
    }

    public void initParams() {
        setForme1(true);
        setForme2(false);
        setForme3(false);
        setItems(null);
        setDateEclos(null);
        setDateIncubes(null);
        setDateTrie(null);
        setNextToForme2(false);
        setTrieOeufOAC(null);
        setRestOfReception(null);
        setSelected(null);
    }

    public void moveToIncubation(String path) {
        initParams();
        SessionUtil.redirectToPage(path);
    }

    public void fromForm2ToForm3() {
        if (DateUtil.getSqlDateToSaveInDB(dateEclos).compareTo(DateUtil.getSqlDateToSaveInDB(dateIncubes)) >= 0) {
            setForme2(false);
            setForme3(true);
            getSelected().setDateIncubation(DateUtil.getSqlDateToSaveInDB(dateIncubes));
            return;
        }
        MessageUtil.fatal("La date d'eclosion doit etre supérieur au cel d'incubation ");
    }

    public void searchByDate() {
        setSelected(ejbFacade.findByDate(DateUtil.getSqlDateToSaveInDB(dateIncubes)));
        if (getSelected().getId() != null) {
            setDateEclos(formatDateToString(getSelected().getEclosion().getDateEclosion()));
            setTrieOeufOAC(getSelected().getTrieOeuf());
            setRestOfReception(trieOeufFacade.restOfReception(getTrieOeufOAC()));
            return;
        }
        MessageUtil.info("Aucun incubation trouvé pour la date : " + dateIncubes);
    }

    public void goToModify() {
        setSelectedToModify(ejbFacade.clone(selected));
        SessionUtil.redirectToPage("incubation.xhtml");
    }

    public String formatDateToString(Date date) {
        return DateUtil.formateDate("dd/MM/yyyy", date);
    }
}
