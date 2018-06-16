package controller;

import bean.CategorieOeuf;
import bean.Eclosion;
import bean.TrieOeuf;

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
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import service.IncubationFacade;
import service.TrieOeufFacade;
import util.DateUtil;
import util.MessageUtil;
import util.SessionUtil;

@Named("eclosionController")
@SessionScoped
public class EclosionController implements Serializable {

    @EJB
    private service.EclosionFacade ejbFacade;
    @EJB
    private TrieOeufFacade trieOeufFacade;
    @EJB
    private IncubationFacade incubationFacade;
    private List<Eclosion> items;
    private Eclosion selected;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private boolean result;
    private String dateIncub;
    private String dateTrie;
    private String dateEclos;
    private TrieOeuf trieOeuf;
    private final IncubationController incubationController = new IncubationController();
    private String dateMax;
    private String dateMin;
    private LineChartModel lineChartModel;

    public LineChartModel getLineChartModel() {
        return lineChartModel;
    }

    public void setLineChartModel(LineChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    public String getDateMax() {
        return dateMax;
    }

    public void setDateMax(String dateMax) {
        this.dateMax = dateMax;
    }

    public String getDateMin() {
        return dateMin;
    }

    public void setDateMin(String dateMin) {
        this.dateMin = dateMin;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void initParams() {
        setForme1(true);
        setForme2(false);
        setForme3(false);
        setDateEclos("");
        setDateIncub("");
        setDateTrie("");
        setTrieOeuf(null);
        setSelected(null);
        setItems(null);
        setDateMax("");
        setDateMin("");
        setLineChartModel(null);
    }

    public void moveToEclosion(String path) {
        initParams();
        SessionUtil.redirectToPage(path);
    }

    public boolean isForme3() {
        System.out.println("h selecetd ==> " + selected);
        System.out.println("h l forme 3 ==> " + forme3);
        return forme3;
    }

    public void setForme3(boolean forme3) {
        this.forme3 = forme3;
    }

    public boolean isForme2() {
        return forme2;
    }

    public void setForme2(boolean forme2) {
        this.forme2 = forme2;
    }

    public TrieOeuf getTrieOeuf() {
        if (trieOeuf == null) {
            trieOeuf = new TrieOeuf();
        }
        return trieOeuf;
    }

    public void setTrieOeuf(TrieOeuf trieOeuf) {
        this.trieOeuf = trieOeuf;
    }

    public String getDateIncub() {
        return dateIncub;
    }

    public void setDateIncub(String dateIncub) {
        this.dateIncub = dateIncub;
    }

    public String getDateTrie() {
        return dateTrie;
    }

    public void setDateTrie(String dateTrie) {
        this.dateTrie = dateTrie;
    }

    public String getDateEclos() {
        return dateEclos;
    }

    public void setDateEclos(String dateEclos) {
        this.dateEclos = dateEclos;
    }

    public boolean isForme1() {
        return forme1;
    }

    public void setForme1(boolean forme1) {
        this.forme1 = forme1;
    }

    public EclosionController() {
    }

    public Eclosion getSelected() {
        if (selected == null) {
            selected = new Eclosion();
        }
        return selected;
    }

    public void setSelected(Eclosion selected) {
        this.selected = selected;
    }

    public List<Eclosion> getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    public void setItems(List<Eclosion> items) {
        this.items = items;
    }

    public void findTrieByCriteria() {
        if (dateEclos.equals("") && dateTrie.equals("") && dateIncub.equals("")) {
            MessageUtil.fatal("Il faut indiquer au moins une date pour continuer !");
            setResult(false);
            return;
        }
        setTrieOeuf(trieOeufFacade.findOACByDateInubsOrEclosOrDateTrie(DateUtil.getSqlDateToSaveInDB(dateTrie),
                DateUtil.getSqlDateToSaveInDB(dateIncub), DateUtil.getSqlDateToSaveInDB(dateEclos)));
        System.out.println("hi from findTrieByCriteria ha item " + trieOeuf);
        if (getTrieOeuf().getId() == null) {
            MessageUtil.info("Le trie que vous cherché n'existe pas");
            setResult(false);
        } else if (getTrieOeuf().getIncubation().getId() == null) {
            MessageUtil.error("Ce trie n'a pas d'incubation");
            setResult(false);
            setTrieOeuf(null);
        } else {
            setResult(true);
            System.out.println("ha trie in setResussusu " + getTrieOeuf());
        }
        System.out.println("ha trie => " + getTrieOeuf());

    }

    public void save() {
        if (getTrieOeuf().getId() == null) {
            MessageUtil.fatal("Chercher un trie pour continuer !");
            return;
        }
        ejbFacade.save(selected);
        MessageUtil.info("L'eclosion pour la date " + formatDate(selected.getDateEclosion()) + " est enregistré");
        initParams();
    }

    public void from1To2() {
        if (trieOeuf != null) {
            setSelected(trieOeuf.getIncubation().getEclosion());
            if (getSelected().getQteEclos() == null) {
                setForme1(false);
                setForme2(true);
                setResult(false);
                return;
            }
            MessageUtil.info("Le trie cherché a déja un eclosion enregistré ");
            System.out.println("ha selected ::" + selected);
        }
    }

    public String formatDate(Date date) {
        return DateUtil.formateDate("dd/MM/yyyy", date);
    }

    public void fillEcarts() {
        if (getSelected().getId() != null) {
            if (selected.getQteEclos().intValue() > selected.getIncubation().getQteIncube().intValue()) {
                MessageUtil.warn("La quantité des oeufs eclos est supérieur à la quantié incubée !");
                return;
            }
            if (selected.getCommercialise().intValue() > selected.getQteEclos().intValue()) {
                MessageUtil.warn("La quantité des oeufs commercialisés est supérieur à la quantié eclos !");
                return;
            }
            selected.setEcartEclosion(ejbFacade.calcEcartEclos(selected));
            selected.setEcartTrie(ejbFacade.ecartTrie(selected));
            setForme2(false);
            setForme3(true);
            return;
        }
        MessageUtil.fatal("Pas d'eclosion, pour calculer les ecarts !!");
    }

    public void validateInputs(FacesContext context, UIComponent component, Object value) {
        BigDecimal input = (BigDecimal) value;
        if (input != null) {
            if (input.intValue() < 0) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_FATAL, "Le nombre des oeufs doit etre positive !", "Pas de détail"));
            }
        }
    }

    public void searchByDate() {
        setSelected(ejbFacade.findByDate(DateUtil.getSqlDateToSaveInDB(dateEclos)));
        if (selected == null) {
            MessageUtil.info("Pas d'éclosion pour la date " + dateEclos);
        }
    }

    public void goToPage(String page) {
        SessionUtil.redirectToPage(page);
    }

    private void setUpChart(LineChartModel chart) {
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
        chart.getAxes().put(AxisType.X, axis);
    }

    public void chart() {
        LineChartModel chart;
        chart = new LineChartModel();
        setItems(ejbFacade.findByDateMinAndMax(DateUtil.getSqlDateToSaveInDB(dateMax), DateUtil.getSqlDateToSaveInDB(dateMin)));
        if (items == null || items.isEmpty()) {
            MessageUtil.fatal("Pas de tries trouvés !");
            return;
        }
        System.out.println("ha items ==> " + items);
        fillTheChart(chart);
        setUpChart(chart);
        setLineChartModel(chart);
    }

    private void fillTheChart(LineChartModel chart) {
        LineChartSeries series1 = new LineChartSeries("Eclos");
        LineChartSeries series2 = new LineChartSeries("Ecart trie");
        LineChartSeries series3 = new LineChartSeries("Ecart eclosion");
        LineChartSeries series4 = new LineChartSeries("Commercialisés");
        for (int j = 0; j < items.size(); j++) {
            Eclosion itemToInsertInSeries = items.get(j);
            System.out.println("ha item =" + itemToInsertInSeries);
            series1.set(DateUtil.formateDate("yyyy-MM-dd", itemToInsertInSeries.getDateEclosion()), itemToInsertInSeries.getQteEclos());
            series2.set(DateUtil.formateDate("yyyy-MM-dd", itemToInsertInSeries.getDateEclosion()), itemToInsertInSeries.getEcartTrie());
            series3.set(DateUtil.formateDate("yyyy-MM-dd", itemToInsertInSeries.getDateEclosion()), itemToInsertInSeries.getEcartEclosion());
            series4.set(DateUtil.formateDate("yyyy-MM-dd", itemToInsertInSeries.getDateEclosion()), itemToInsertInSeries.getCommercialise());
        }
        chart.addSeries(series1);
        chart.addSeries(series2);
        chart.addSeries(series3);
        chart.addSeries(series4);
    }

}
