/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import bean.CategorieOeuf;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author lotfi
 */
public class Excel {

    private WritableFont fontHeader;
    private WritableFont fontCategorieOeuf;
    private WritableCellFormat cellFormatHeader;

    public void setFontHeader(WritableFont fontHeader) {
        this.fontHeader = fontHeader;
    }

    public void setCellFormatHeader(WritableCellFormat cellFormatHeader) {
        this.cellFormatHeader = cellFormatHeader;
    }

    public static WritableWorkbook createWorkBook(String path) throws IOException {
        return Workbook.createWorkbook(new File(path));
    }

    public static WritableSheet createSheet(WritableWorkbook workbook, String nom, int index) {
        return workbook.createSheet(nom, index);
    }

    public static void addLabel(WritableSheet sheet, int column, int row, String labelContent, WritableCellFormat cellFormat) throws WriteException {
        Label label = new Label(column, row, labelContent, cellFormat);
        sheet.addCell(label);
    }

    public static void addNumber(WritableSheet sheet, int column, int row, Integer integer, WritableCellFormat cellFormat) throws WriteException {
        jxl.write.Number number = new jxl.write.Number(column, row, integer, cellFormat);
        sheet.addCell(number);
    }

    public static void addDate(WritableSheet sheet, int column, int row, Date date, WritableCellFormat cellFormat) throws WriteException {
        DateFormat customDateFormat = new DateFormat("dd/MM/yyyy");
        WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);
        DateTime dateCell = new DateTime(0, 6, date, dateFormat);
        sheet.addCell(dateCell);
    }

    public static void writeAndCloseWorkbook(WritableWorkbook workbook) throws IOException, WriteException {
        workbook.write();
        workbook.close();
    }

    public static WritableCellFormat centerContentInCell(WritableCellFormat cellFormat) throws WriteException {
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        return cellFormat;
    }

    public static void main(String[] args) throws IOException, WriteException {
        int startRowAt = 5;
        String path = "/home/lotfi/test.xlsx";
        WritableWorkbook workbook = createWorkBook(path);
        WritableSheet sheet = createSheet(workbook, "Trie Oeufs", 0);

        WritableFont fontHeader = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.SINGLE, Colour.BLUE_GREY);
        WritableFont fontAttributs = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

        WritableCellFormat cellFormatCategorie = centerContentInCell(new WritableCellFormat(fontHeader));
        cellFormatCategorie.setBackground(Colour.BRIGHT_GREEN);

        WritableCellFormat totalPertes = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD));
        totalPertes.setBackground(Colour.SKY_BLUE);

        writeLeftTitles(sheet, fontHeader, startRowAt, cellFormatCategorie, fontAttributs, totalPertes);

        applicateMegesOnTheSheet(sheet);
        writeAndCloseWorkbook(workbook);
    }

    public static void writeLeftTitles(WritableSheet sheet, WritableFont fontHeader1, int startRowAt, WritableCellFormat cellFormatCategorie, WritableFont fontAttributs, WritableCellFormat totalPertes) throws WriteException {
        addLabel(sheet, 0, 0, "Jour", centerContentInCell(new WritableCellFormat(fontHeader1)));
        addLabel(sheet, 0, 3, "Réception", centerContentInCell(new WritableCellFormat(fontHeader1)));
        for (CategorieOeuf categorieOeuf : findAll()) {
            addLabel(sheet, 0, startRowAt, categorieOeuf.getDesignation(), cellFormatCategorie);
            startRowAt = addNamesOfAttributs(categorieOeuf, sheet, startRowAt + 1, new WritableCellFormat(fontAttributs));
        }
        addLabel(sheet, 0, sheet.getRows() + 1, "Total des pertes", totalPertes);
    }

    public static void applicateMegesOnTheSheet(WritableSheet sheet) throws WriteException {
        mergeRows(sheet, 0, 0, 2, 20);
        mergeRows(sheet, 0, 3, 4, 20);
    }

    private static void mergeColumns(WritableSheet sheet, int row, int colStart, int colEnd, int sizeCell) throws WriteException {
        sheet.mergeCells(colStart, row, colEnd, row);
        sheet.setRowView(row, sizeCell);
    }

    private static void mergeRows(WritableSheet sheet, int col, int rowStart, int rowEnd, int sizeCell) throws WriteException {
        sheet.mergeCells(col, rowStart, col, rowEnd);
        sheet.setColumnView(col, sizeCell);
    }

    public static List<CategorieOeuf> findAll() {
        List<CategorieOeuf> categorieOeufs = new ArrayList();
        categorieOeufs.add(new CategorieOeuf(1l, "OAC"));
        categorieOeufs.add(new CategorieOeuf(2l, "DEMARRAGE"));
        categorieOeufs.add(new CategorieOeuf(2l, "SALES"));
        categorieOeufs.add(new CategorieOeuf(2l, "DJ"));
        categorieOeufs.add(new CategorieOeuf(2l, "NORMALES"));
        categorieOeufs.add(new CategorieOeuf(2l, "CASES"));
        return categorieOeufs;
    }

    public static int addNamesOfAttributs(CategorieOeuf categorieOeuf, WritableSheet sheet, int startRowAt, WritableCellFormat cellFormat) throws WriteException {
        if (categorieOeuf.getDesignation().equals("OAC")) {
            attributsOfOAC(sheet, startRowAt, cellFormat);
            return startRowAt + 7;
        }
        attributsOfOthersCategories(sheet, startRowAt, cellFormat);
        return startRowAt + 6;
    }

    private static void attributsOfOthersCategories(WritableSheet sheet, int startRow, WritableCellFormat cellFormat) throws WriteException {
        addLabel(sheet, 0, startRow, "SI", cellFormat);
        addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        addLabel(sheet, 0, startRow + 2, "Ventes", cellFormat);
        addLabel(sheet, 0, startRow + 3, "Don", cellFormat);
        addLabel(sheet, 0, startRow + 4, "Perte", cellFormat);
        addLabel(sheet, 0, startRow + 5, "Démarrage", cellFormat);
        addLabel(sheet, 0, startRow + 6, "SF", cellFormat);
    }

    private static void attributsOfOAC(WritableSheet sheet, int startRow, WritableCellFormat cellFormat) throws WriteException {
        addLabel(sheet, 0, startRow, "SI", cellFormat);
        addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        addLabel(sheet, 0, startRow + 2, "Mise en incubation", cellFormat);
        addLabel(sheet, 0, startRow + 3, "Ventes", cellFormat);
        addLabel(sheet, 0, startRow + 4, "Don", cellFormat);
        addLabel(sheet, 0, startRow + 5, "Perte", cellFormat);
        addLabel(sheet, 0, startRow + 6, "Démarrage", cellFormat);
        addLabel(sheet, 0, startRow + 7, "SF", cellFormat);
    }

}
