package controller;

import bean.Eclosion;
import bean.TrieOeuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import service.TrieOeufFacade;
import util.DateUtil;
import util.MessageUtil;

@Named("eclosionController")
@SessionScoped
public class EclosionController implements Serializable {

    @EJB
    private service.EclosionFacade ejbFacade;
    @EJB
    private TrieOeufFacade trieOeufFacade;
    private List<Eclosion> items = null;
    private Eclosion selected;
    private boolean forme1 = true;
    private boolean forme2;
    private boolean forme3;
    private String dateIncub;
    private String dateTrie;
    private String dateEclos;
    private TrieOeuf trieOeuf;

    public boolean isForme3() {
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
            MessageUtil.fatal("Il faut indiquer au moins un date pour continuer !");
            return;
        }
        trieOeuf = trieOeufFacade.findOACByDateInubsOrEclosOrDateTrie(DateUtil.getSqlDateToSaveInDB(dateTrie),
                DateUtil.getSqlDateToSaveInDB(dateIncub), DateUtil.getSqlDateToSaveInDB(dateEclos));
        if (getTrieOeuf().getId() == null) {
            MessageUtil.info("Le trie que vous cherché n'existe pas");
        } else if (getTrieOeuf().getIncubation().getId() == null) {
            MessageUtil.error("Ce trie n'a pas d'incubation");
            setTrieOeuf(null);
        }
        System.out.println("ha trie => " + getTrieOeuf());
    }

    public void save() {
        if (getTrieOeuf().getId() == null) {
            MessageUtil.fatal("Chercher un trie pour continuer !");
            return;
        }
        ejbFacade.save(selected);
    }

    public void from1To2() {
        if (trieOeuf != null) {
            setForme1(false);
            setForme2(true);
            setSelected(trieOeuf.getIncubation().getEclosion());
        }
    }
}
