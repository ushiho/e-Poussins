/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package document;

import bean.CategorieOeuf;
import bean.TrieOeuf;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import service.CategorieOeufFacade;
import service.TrieOeufFacade;
import util.ExcelUtil;

/**
 *
 * @author lotfi
 */
public class TrieOeufsExcel {

    private final TrieOeufFacade trieOeufFacade = new TrieOeufFacade();
    private final CategorieOeufFacade categorieOeufFacade = new CategorieOeufFacade();
    private List<TrieOeuf> trieOeufs;
    private List<CategorieOeuf> categorieOeufs;
    private int rowCounter;
    private final int START_ROW_AT = 5;

    public void setTrieOeufs(List<TrieOeuf> trieOeufs) {
        this.trieOeufs = trieOeufs;
    }

    public void setCategorieOeufs(List<CategorieOeuf> categorieOeufs) {
        this.categorieOeufs = categorieOeufs;
    }

    public File write(String fileName) throws IOException, WriteException {

        String path = "/home/lotfi/glassfish-4.1.1/ExcelDocs/" + fileName;
        WritableWorkbook workbook = ExcelUtil.createWorkBook(path);
        WritableSheet sheet = ExcelUtil.createSheet(workbook, "Trie Oeufs", 0);

        writeLeftTitles(sheet, START_ROW_AT, fontAttributs(Colour.BLACK));

        fillContent(sheet);

        ExcelUtil.writeAndCloseWorkbook(workbook);
        return new File(path);
    }

    private WritableCellFormat stylingTotalPerteCell() throws WriteException {
        WritableCellFormat totalPertes = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD));
        totalPertes.setBackground(Colour.SKY_BLUE);
        return totalPertes;
    }

    private WritableCellFormat stylingCategorieCell() throws WriteException {
        WritableCellFormat cellFormatCategorie = ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader(Colour.BLUE_GREY)));
        cellFormatCategorie.setBackground(Colour.BRIGHT_GREEN);
        return cellFormatCategorie;
    }

    public void writeLeftTitles(WritableSheet sheet, int startRowAt, WritableFont fontAttributs) throws WriteException {
        ExcelUtil.addLabel(sheet, 0, 0, "Semaine", ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader(Colour.BLUE_GREY))));
        ExcelUtil.addLabel(sheet, 0, 1, "Jour", ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader(Colour.BLUE_GREY))));
        ExcelUtil.mergeRows(sheet, 0, 1, 2, 20);
        ExcelUtil.addLabel(sheet, 0, 3, "Réception", ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader(Colour.BLUE_GREY))));
        ExcelUtil.mergeRows(sheet, 0, 3, 4, 20);
        for (CategorieOeuf categorieOeuf : this.categorieOeufs) {
            ExcelUtil.addLabel(sheet, 0, startRowAt, categorieOeuf.getDesignation(), stylingCategorieCell());
            startRowAt = addNamesOfAttributs(categorieOeuf, sheet, startRowAt + 1, new WritableCellFormat(fontAttributs));
        }
        rowCounter = sheet.getRows();
        ExcelUtil.addLabel(sheet, 0, rowCounter, "Total des pertes", stylingTotalPerteCell());
    }

    public int addNamesOfAttributs(CategorieOeuf categorieOeuf, WritableSheet sheet, int startRowAt, WritableCellFormat cellFormat) throws WriteException {
        if (categorieOeuf.getDesignation().equals("OAC")) {
            attributsOfOAC(sheet, startRowAt, cellFormat);
            return startRowAt + 7;
        }
        attributsOfOthersCategories(sheet, startRowAt, cellFormat);
        return startRowAt + 6;
    }

    private void attributsOfOthersCategories(WritableSheet sheet, int startRow, WritableCellFormat cellFormat) throws WriteException {
//        int rowsToAdd = 0;
        ExcelUtil.addLabel(sheet, 0, startRow, "SI", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 2, "Ventes", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 3, "Don", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 4, "Perte", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 5, "SF", cellFormat);
    }

    private void attributsOfOAC(WritableSheet sheet, int startRow, WritableCellFormat cellFormat) throws WriteException {
        ExcelUtil.addLabel(sheet, 0, startRow, "SI", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 2, "Mise en incubation", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 3, "Ventes", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 4, "Don", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 5, "Perte", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 6, "SF", cellFormat);
    }

    public void fillDatesInSheet(WritableSheet sheet, Date date, int column, int row) throws WriteException {
        System.out.println("cc from fillDatesInSheet ha date : " + date + " ha row " + row + " o ha colomne = > " + column);
        WritableCellFormat cellFormat = new WritableCellFormat(new jxl.write.DateFormat("dd/MM/yyyy"));
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setBackground(Colour.YELLOW);
        cellFormat.setFont(fontHeader(Colour.BLACK));
        ExcelUtil.addDate(sheet, column, row, date, cellFormat);
    }

    public void fillSemaineInSheet(WritableSheet sheet, int semaine, int columnStart, int columnEnd) throws WriteException {
        WritableCellFormat semaineCell = ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader(Colour.BLUE_GREY)));
        semaineCell.setBackground(Colour.IVORY);
        ExcelUtil.addNumber(sheet, columnStart, 0, semaine, semaineCell);
//        ExcelUtil.mergeColumns(sheet, 0, columnStart, columnEnd, 10);
        System.out.println("from fillSemaine in Shett ha colone start : " + columnStart + " o ha colone end : " + columnEnd);
        System.out.println("ha num d semaine ==> " + semaine);
//        ExcelUtil.mergeColumns(sheet, 0, 1, column, trieOeufFacade.countElementExistInWeek(trieOeufs, semaine));
    }

    public void fillReception(WritableSheet sheet, int column, int row, Integer integer) throws WriteException {
        WritableCellFormat receptionCell = new WritableCellFormat(fontHeader(Colour.BLUE_GREY));
        ExcelUtil.centerContentInCell(receptionCell);
        ExcelUtil.addNumber(sheet, column, row, integer, receptionCell);
        ExcelUtil.mergeRows(sheet, column, row, row + 1, 20);
    }

//    public void fillAndMergeAllReceptionRows(WritableSheet sheet) throws WriteException {
//        for (int col = 0; col < this.trieOeufs.size(); col++) {
//            TrieOeuf trieOeuf = this.trieOeufs.get(col);
//
////            fillReception(sheet, col+1, 3, trieOeuf.getReception());
//        }
//        ExcelUtil.mergeRows(sheet, 0, 3, 4, 20);
//    }
    public void fillContent(WritableSheet sheet) throws WriteException {
        List<TrieOeuf> trieOeufsInSameDateAndReception;
        int startColumn = 1;
        while (!this.trieOeufs.isEmpty()) {
            TrieOeuf trieOeuf = trieOeufs.get(0);
            trieOeufsInSameDateAndReception = trieOeufFacade.triesInSameDateAndReception(trieOeufs, trieOeuf.getDateTrie(), trieOeuf.getReception());
            fillContentsInAttributs(trieOeufsInSameDateAndReception, sheet, startColumn);
            startColumn++;
        }
    }

    private void fillContentsInAttributs(List<TrieOeuf> trieOeufsInSameDateAndReception, WritableSheet sheet, int startColumn) throws WriteException {
        TrieOeuf trieOeuf = trieOeufsInSameDateAndReception.get(0);
        fillReception(sheet, startColumn, 3, trieOeuf.getReception().intValue());
        fillDatesInSheet(sheet, trieOeuf.getDateTrie(), startColumn, 1);
        fillTotalPert(sheet, startColumn, sheet.getRows(), trieOeufsInSameDateAndReception);
        fillSemaineInSheet(sheet, trieOeuf.getNumSemaine(), startColumn, trieOeufFacade.countNumberOfColoneInList(trieOeufsInSameDateAndReception, trieOeuf.getNumSemaine()));
        insertTrieValues(trieOeufsInSameDateAndReception, sheet, startColumn);
    }

    private void fillTotalPert(WritableSheet sheet, int col, int row, List<TrieOeuf> trieOeufs) throws WriteException {
        ExcelUtil.addNumber(sheet, col, rowCounter, trieOeufFacade.calculTotalPerte(trieOeufs).intValue(), stylingTotalPerteCell());
    }

    private void insertTrieValues(List<TrieOeuf> trieOeufsInSameDateAndReception, WritableSheet sheet, int startColumn) throws WriteException {
        for (TrieOeuf trieOeufHaveSameDateAndReception : trieOeufsInSameDateAndReception) {

            System.out.println("ha trie from fillContentsInAttributs : " + trieOeufHaveSameDateAndReception);
            int startRow = startRowAt(trieOeufHaveSameDateAndReception.getCategorieOeuf());
            ExcelUtil.addLabel(sheet, startColumn, startRow, "", stylingCategorieCell());
            System.out.println("h row from fillcontent : " + startRow);
            if (trieOeufHaveSameDateAndReception.getCategorieOeuf().getDesignation().equals("OAC")) {
                fillCategorieOacAttributs(sheet, startColumn, startRow + 1, trieOeufHaveSameDateAndReception);
            } else {
                fillOthersCategorieAttributs(sheet, startColumn, startRow + 1, trieOeufHaveSameDateAndReception);
            }
            trieOeufs.remove(trieOeufHaveSameDateAndReception);
        }
    }

    private void fillCategorieOacAttributs(WritableSheet sheet, int startColumn, int startRow, TrieOeuf trieOeufHaveSameDateAndReception) throws WriteException {
        System.out.println("from fillCategorieOacAttributs ha row => " + startRow + " o ha colone :" + startColumn);
        ExcelUtil.addNumber(sheet, startColumn, startRow, trieOeufHaveSameDateAndReception.getSituationInitiale().intValue(), new WritableCellFormat(fontAttributs(Colour.RED)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 1, trieOeufHaveSameDateAndReception.getEntree().intValue(), new WritableCellFormat(fontAttributs(Colour.DARK_GREEN)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 2, trieOeufHaveSameDateAndReception.getMisEnIncubation().intValue(), new WritableCellFormat(fontAttributs(Colour.BROWN)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 3, trieOeufHaveSameDateAndReception.getVente().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 4, trieOeufHaveSameDateAndReception.getDon().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 5, trieOeufHaveSameDateAndReception.getPerte().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 6, trieOeufHaveSameDateAndReception.getSituationFinale().intValue(), new WritableCellFormat(fontAttributs(Colour.RED)));
    }

    private void fillOthersCategorieAttributs(WritableSheet sheet, int startColumn, int startRow, TrieOeuf trieOeufHaveSameDateAndReception) throws WriteException {
        System.out.println("from fillOthersCategorieAttributs ha row => " + startRow + " o ha colone :" + startColumn);
        ExcelUtil.addNumber(sheet, startColumn, startRow, trieOeufHaveSameDateAndReception.getSituationInitiale().intValue(), new WritableCellFormat(fontAttributs(Colour.RED)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 1, trieOeufHaveSameDateAndReception.getEntree().intValue(), new WritableCellFormat(fontAttributs(Colour.DARK_GREEN)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 2, trieOeufHaveSameDateAndReception.getVente().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 3, trieOeufHaveSameDateAndReception.getDon().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 4, trieOeufHaveSameDateAndReception.getPerte().intValue(), new WritableCellFormat(fontAttributs(Colour.BLACK)));
        ExcelUtil.addNumber(sheet, startColumn, startRow + 5, trieOeufHaveSameDateAndReception.getSituationFinale().intValue(), new WritableCellFormat(fontAttributs(Colour.RED)));
    }

//    public int endRowByCategorie(CategorieOeuf categorieOeuf) {
//        int i = 6;
//        if (categorieOeuf.getDesignation().equals("OAC")) {
//            i = 7;
//        }
//        return i;
//    }
    public int startRowAt(CategorieOeuf categorieOeuf) {
        switch (categorieOeuf.getDesignation()) {
            case "OAC":
                System.out.println("from startRowAt => 5");
                return 5;
            case "NORMAL":
                System.out.println("from startRowAt => 13");
                return 13;
            case "SALES":
                System.out.println("from startRowAt => 20");
                return 20;
            case "CASSES":
                System.out.println("from startRowAt => 27");
                return 27;
            case "DEMARRAGE":
                System.out.println("from startRowAt => 34");
                return 34;
            case "DJ":
                System.out.println("from startRowAt => 41");
                return 41;
            default:
                System.out.println("error f switch d startRowAt");
                return 2;
        }
    }

    private WritableFont fontAttributs(Colour colour) {
        return new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, colour);
    }

    private WritableFont fontHeader(Colour colour) {
        return new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.SINGLE, colour);
    }

}
