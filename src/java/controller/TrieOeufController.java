package controller;

import bean.CategorieOeuf;
import bean.TrieOeuf;
import document.TrieOeufsExcel;
import java.io.IOException;
import service.TrieOeufFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import jxl.write.WriteException;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import service.CategorieOeufFacade;
import service.UtilisateurFacade;
import util.DateUtil;
import util.DownloadUtil;
import util.MessageUtil;
import util.SessionUtil;

@Named("trieOeufController")
@SessionScoped
public class TrieOeufController implements Serializable {

    @EJB
    private service.TrieOeufFacade ejbFacade;
    @EJB
    private CategorieOeufFacade categorieOeufFacade;
    @EJB
    private UtilisateurFacade utilisateurFacade;
    private final TrieOeufsExcel trieOeufsExcel = new TrieOeufsExcel();
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
    private TrieOeuf trieOeufToModify;
    private List<TrieOeuf> trieOeufsToDownload;
    //attr for statics
    private String dateMin;
    private String dateMax;
    private LineChartModel LineChartModel;
    private PieChartModel pieChartModel;
    private BarChartModel barChartModel;

    public BarChartModel getBarChartModel() {
        return barChartModel;
    }

    public void setBarChartModel(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    public PieChartModel getPieChartModel() {
        return pieChartModel;
    }

    public void setPieChartModel(PieChartModel pieChartModel) {
        this.pieChartModel = pieChartModel;
    }

    public LineChartModel getLineChartModel() {
        return LineChartModel;
    }

    public void setLineChartModel(LineChartModel LineChartModel) {
        this.LineChartModel = LineChartModel;
    }

    public String getDateMin() {
        return dateMin;
    }

    public void setDateMin(String dateMin) {
        this.dateMin = dateMin;
    }

    public String getDateMax() {
        return dateMax;
    }

    public void setDateMax(String dateMax) {
        this.dateMax = dateMax;
    }

    public List<TrieOeuf> getTrieOeufsToDownload() {
        return trieOeufsToDownload;
    }

    public void setTrieOeufsToDownload(List<TrieOeuf> trieOeufsToDownload) {
        System.out.println("ha trie to insert in download =W>> " + items);
        this.trieOeufsToDownload = trieOeufsToDownload;
        System.out.println("after from download " + trieOeufsToDownload);
    }

    public TrieOeuf getTrieOeufToModify() {
        System.out.println("cc is get modiy = " + trieOeufToModify);
        if (trieOeufToModify == null) {
            trieOeufToModify = new TrieOeuf();
        }
        return trieOeufToModify;
    }

    public void setTrieOeufToModify(TrieOeuf trieOeufToModify) {
        this.trieOeufToModify = trieOeufToModify;
    }

    public TrieOeufsExcel getTrieOeufsExcel() {
        return trieOeufsExcel;
    }

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
        if (totalEntres == null) {
            return new BigDecimal(0);
        }
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
        if (semaine == null || semaine < 0) {
            this.semaine = null;
            return;
        }
        this.semaine = semaine;
        System.out.println("cc is etSemaine : " + semaine);
    }

    public void weekOfSelectedDate() {
        System.out.println("cc from weekOfSelectedDate ");
        setSemaine(DateUtil.weekNumberFromDate(DateUtil.getSqlDateToSaveInDB(dateTrie)));
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

    public void addToList() {
        remplirSelected();
        if (categorieOeufFacade.categorieOeufsExiste(getCategorieOeufsAdded(), getCategorieOeufSelected())) {
            MessageUtil.showMsg("Cette catégorie est déja ajoutée");
            initSelectdAndCategorie();
            return;
        }
        getItems().add(ejbFacade.clone(getSelected()));
        getCategorieOeufsAdded().add(categorieOeufFacade.clone(categorieOeufSelected));
        setRestReception(getRestReception().subtract(getSelected().getEntree()));
        setTotalEntres(getTotalEntres().add(getSelected().getEntree()));
        MessageUtil.info("La Catégorie '" + selected.getCategorieOeuf().getDesignation() + "' est ajoutée");
        initSelectdAndCategorie();
        setForme3(false);
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
        if (selected != null && selectedToModify != null) {
            calculRestReceptionAndEntree();
            selected.setCategorieOeuf(categorieOeufSelected);
            MessageUtil.info("La catégorie '" + categorieOeufSelected.getDesignation() + "' est bien modifiée");
            setSelectedToModify(null);
            setForme3(false);
            initSelectdAndCategorie();
        }
    }

    private void calculRestReceptionAndEntree() {
        setRestReception(getRestReception().subtract(getSelected().getEntree()));
        setTotalEntres(getTotalEntres().subtract(getSelectedToModify().getEntree()));
        setTotalEntres(getTotalEntres().add(getSelected().getEntree()));
    }

    public void reloadAllThePage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void removeFromList() {
        if (selected != null) {
            System.out.println("before ha liset : " + items);
            getItems().remove(ejbFacade.indexOfSelected(items, selected));
            setRestReception(getRestReception().add(selected.getEntree()));
            setTotalEntres(getTotalEntres().subtract(selected.getEntree()));
            MessageUtil.info("La catégorie '" + getSelected().getCategorieOeuf().getDesignation() + "' est supprimée");
            getCategorieOeufsAdded().remove(getSelected().getCategorieOeuf());
            initSelectdAndCategorie();
            System.out.println("after ha liste =< " + getItems());
        }
    }

    public void setSIToTheSelected() {
        System.out.println("cc c setSIToTheSelected ha date : " + dateTrie);
        System.out.println("cc c setSIToTheSelected ha sql date bd : " + DateUtil.getSqlDateToSaveInDB(dateTrie));
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
        if (getRestReception().compareTo(new BigDecimal(0)) == 0 && !getItems().isEmpty()) {
            System.out.println("from save in db before ha items => " + items);
            System.out.println("from save in db before download => " + trieOeufsToDownload);
            setTrieOeufsToDownload(ejbFacade.cloneList(getItems()));
            ejbFacade.saveListToDB(trieOeufsToDownload);
            MessageUtil.info("Vos donnés sont bien enregistrés ");
            setForme4(true);
            setForme3(false);
            setForme2(false);
            getItems().clear();
            System.out.println("after ha trieTo download==> " + trieOeufsToDownload);
            System.out.println("after o ha items ==> " + items);
            return;
        }
        MessageUtil.addMessage("Il vous reste " + getRestReception() + " oeufs ,Choisie une catégorie"
                + "pour les suvergarder .");
    }

    public void cancelSavingInDb() {
        setForme1(true);
        initParamsUsedInTrie();
    }

    public void initParamsUsedInTrie() {
        System.out.println("cc is initTrie");
        setSelectedToModify(null);
        setReception(null);
        setRestReception(null);
        setTotalEntres(null);
        setDateTrie("");
        setForme2(false);
        setForme3(false);
        setForme1(true);
        setForme4(false);
        initSelectdAndCategorie();
        setCategorieOeufsAdded(null);
        setItems(null);
        setSemaine(null);
        setDateMax("");
        setDateMin("");
        setLineChartModel(null);
        setBarChartModel(null);
        setPieChartModel(null);
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

    public void imprimer(List<TrieOeuf> trieOeufs) throws IOException, WriteException {
        if (trieOeufs != null && !trieOeufs.isEmpty()) {
            ejbFacade.arrangeTrieByDate(trieOeufs);
            trieOeufsExcel.setTrieOeufs(trieOeufs);
            trieOeufsExcel.setCategorieOeufs(categorieOeufFacade.findAll());
            DownloadUtil.downloadFile(trieOeufsExcel.write("trieOeufs.xlsx"));
            MessageUtil.info("Fichier Excel crée avec succes");
            return;
        }
        MessageUtil.fatal("Pas de fichier à télécharger !!");
    }

    // methods for suivis Templates
    public List<Integer> chargeYearsSavedInDB() {
        return ejbFacade.yearsBetweenTwoDate(ejbFacade.minOrMaxDateExisteInDB(2), ejbFacade.minOrMaxDateExisteInDB(1));
    }

    public void searchJournalier() {
        setItems(ejbFacade.searchJournalier(year, mois, inputMin, inputMax, categorieOeufSelected));
        testListAndShowMsg();
    }

    public void testListAndShowMsg() {
        if (ejbFacade.testItems(items)) {
            MessageUtil.info("Pas de tries trouvés pour vos constraintes !");
        }
    }

    public int maxDayInSelectedMonth() {
        if (mois != null && year != null) {
            System.out.println("ha day max : " + DateUtil.maxDayInMonthOfYear(year, mois));
            return DateUtil.maxDayInMonthOfYear(year, mois);
        }
        return 31;
    }

    public void initParamsUsedInSuivis() {
        initSelectdAndCategorie();
        setYear(null);
        setMois(null);
        setInputMax(null);
        setInputMin(null);
        setItems(null);
    }

    // methods for hebdo template
    public void searchHebdo() {
        setItems(ejbFacade.findByNumSemaineOrCategorie(year, inputMax, inputMin, categorieOeufSelected));
        testListAndShowMsg();
    }

    public void validateJour(FacesContext context, UIComponent component, Object value) {
        Integer input = (Integer) value;
        if (input != null) {
            if (input < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "le nombre des jours est positif !", "Pas de détail"));
            } else if (input > 31) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "le jour max est 31 !", "Pas de détail"));

            }
        }
    }

    public void validateDays(FacesContext context, UIComponent component, Object value) {
        BigDecimal input = (BigDecimal) value;
        if (input != null) {
            if (input.intValue() < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "le nombre des oeufs est positif !", "Pas de détail"));
            }
        }
    }

    public void validateWeeks(FacesContext context, UIComponent component, Object value) {
        Integer input = (Integer) value;
        if (input != null) {
            if (input < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "le nombre des semains est positif !", "Pas de détail"));
            }
        }
    }

    public void moveFromTrie(String path) {
        System.out.println("go to " + path);
        initParamsUsedInTrie();
        SessionUtil.redirectToPage(path);
    }

    public void moveFromSuivis(String path) {
        System.out.println("hi is moveFromSuivis");
        initParamsUsedInSuivis();
        SessionUtil.redirectToPage(path);
    }

    public void validateInputs(FacesContext context, UIComponent component, Object value) {
        BigDecimal input = (BigDecimal) value;
        if (input != null) {
            if (input.intValue() < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "Le nombre des oeufs doit etre positive !", "Pas de détail"));
            }
        }
    }

    public void testIfTrieExistAndPassToForm2() {
        setTrieOeufToModify(ejbFacade.findByDate(DateUtil.getSqlDateToSaveInDB(dateTrie)));
        if (getTrieOeufToModify().getId() == null) {
            setForme1(false);
            setForme2(true);
            return;
        }
        MessageUtil.error("Les tries pour cet date sont déja enregistré");
    }

    public void imprimerTrieAdded() throws IOException, WriteException {
        imprimer(trieOeufsToDownload);
        initParamsUsedInTrie();
    }

    private void setUpLineChart(LineChartModel chart) {
        chart.setTitle("Production journalier des oeufs");
        chart.setZoom(true);
        chart.setAnimate(true);
        chart.setLegendPosition("ne");
        Axis yAxis = chart.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setLabel("Quantité");
        DateAxis axis = new DateAxis("Jours");
        axis.setMin(DateUtil.formateDate("yyyy-MM-dd", DateUtil.getSqlDateToSaveInDB(dateMin)));
        axis.setMax(DateUtil.formateDate("yyyy-MM-dd", DateUtil.getSqlDateToSaveInDB(dateMax)));
        axis.setTickFormat("%#d/%#m/%Y");
        axis.setTickAngle(-50);
        chart.getAxes().put(AxisType.X, axis);
    }

    public void lineChart() {
        setLineChartModel(new LineChartModel());
        LineChartModel chart;
        chart = new LineChartModel();
        setItems(ejbFacade.findByDateMinOrMaxOrCategorie(DateUtil.getSqlDateToSaveInDB(dateMin), DateUtil.getSqlDateToSaveInDB(dateMax), categorieOeufSelected));
        if (items == null || items.isEmpty()) {
            MessageUtil.fatal("Pas de tries trouvés !");
            return;
        }
        fillTheLineChart(chart);
        setUpLineChart(chart);
        setLineChartModel(chart);
    }

    private void fillTheLineChart(LineChartModel chart) {
        for (int i = 0; i < items.size(); i++) {
            TrieOeuf trieOeuf = items.get(i);
            CategorieOeuf categorieOeuf = trieOeuf.getCategorieOeuf();
            LineChartSeries series = new LineChartSeries();
            series.setLabel(categorieOeuf.getDesignation());
            for (int j = 0; j < items.size(); j++) {
                TrieOeuf itemToInsertInSeries = items.get(j);
                if (itemToInsertInSeries.getCategorieOeuf().getId() == categorieOeuf.getId()) {
                    series.set(DateUtil.formateDate("yyyy-MM-dd", itemToInsertInSeries.getDateTrie()), itemToInsertInSeries.getSituationFinale());
                    items.remove(itemToInsertInSeries);
                    i = 0;
                }
            }
            chart.addSeries(series);
        }
    }

    public void searchByDate() {
        System.out.println("cc is search by date");
        setSelected(ejbFacade.findByDateAndCategorie(DateUtil.getSqlDateToSaveInDB(dateTrie), categorieOeufSelected));
        if (getSelected().getId() == null) {
            MessageUtil.fatal("Pas de trie pour la date " + dateTrie);
            return;
        }
        createPieModel();
        initBarModel();
        setRestReception(ejbFacade.restOfReception(getSelected()));
    }

    public void createPieModel() {
        if (selected == null) {
            MessageUtil.fatal("Pas d'éclosion pour faire les statistiques, Consulter un ");
            return;
        }
        PieChartModel pieModel2 = new PieChartModel();

        pieModel2.set("S.I + Entrée", getSelected().getSituationInitiale().add(getSelected().getEntree()));
        if (getSelected().getCategorieOeuf().getDesignation().equals("OAC")) {
            pieModel2.set("Incubés", getSelected().getMisEnIncubation());
        }
        pieModel2.set("Ventes", getSelected().getVente());
        pieModel2.set("Pertes", getSelected().getPerte());
        pieModel2.set("Don", getSelected().getDon());

        pieModel2.setTitle("Les oeufs triés(" + getSelected().getCategorieOeuf().getDesignation() + ") pour la date : " + dateTrie);
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
        setPieChartModel(pieModel2);
    }

    private void initBarModel() {
        System.out.println("hi is initBar");
        BarChartModel model = new BarChartModel();
        int i = 0;
        for (TrieOeuf item : ejbFacade.findAllByDate(DateUtil.getSqlDateToSaveInDB(dateTrie))) {
            System.out.println("series " + i + "' est crees");
            ChartSeries series = new ChartSeries(item.getCategorieOeuf().getDesignation());
            series.set(DateUtil.formateDate("yyyy-MM-dd", item.getDateTrie()), item.getEntree());
            model.addSeries(series);
            System.out.println("ha series" + i + " ==> " + series);
            i++;
        }
        setBarChartModel(model);
        setUpBarChart(getBarChartModel());
    }

    private void setUpBarChart(BarChartModel chart) {
        chart.setTitle("Les entrés des différents catégorie pour la date : " + dateTrie);
        chart.setZoom(true);
        chart.setAnimate(true);
        chart.setLegendPosition("ne");
        Axis yAxis = chart.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setLabel("Quantité");
//        DateAxis axis = new DateAxis("Jours");
//        axis.setTickFormat("%#d/%#m/%Y");
        Axis xAxis = chart.getAxis(AxisType.X);
        xAxis.setLabel("Jours");
        xAxis.setTickFormat("%#d/%#m/%Y");
    }
}
