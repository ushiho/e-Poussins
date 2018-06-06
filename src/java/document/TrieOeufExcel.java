/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package document;

import bean.CategorieOeuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import util.ExcelUtil;

/**
 *
 * @author lotfi
 */
public class TrieOeufExcel {

    public static void main(String[] args) throws IOException, WriteException {
        int startRowAt = 5;
        String path = "/home/lotfi/test.xlsx";
        WritableWorkbook workbook = ExcelUtil.createWorkBook(path);
        WritableSheet sheet = ExcelUtil.createSheet(workbook, "Trie Oeufs", 0);

        WritableFont fontHeader = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.SINGLE, Colour.BLUE_GREY);
        WritableFont fontAttributs = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

        WritableCellFormat cellFormatCategorie = ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader));
        cellFormatCategorie.setBackground(Colour.BRIGHT_GREEN);

        WritableCellFormat totalPertes = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD));
        totalPertes.setBackground(Colour.SKY_BLUE);

        writeLeftTitles(sheet, fontHeader, startRowAt, cellFormatCategorie, fontAttributs, totalPertes);

        applicateMegesOnTheSheet(sheet);
        
        ExcelUtil.writeAndCloseWorkbook(workbook);
    }

    public static void writeLeftTitles(WritableSheet sheet, WritableFont fontHeader1, int startRowAt, WritableCellFormat cellFormatCategorie, WritableFont fontAttributs, WritableCellFormat totalPertes) throws WriteException {
        ExcelUtil.addLabel(sheet, 0, 0, "Jour", ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader1)));
        ExcelUtil.addLabel(sheet, 0, 3, "Réception", ExcelUtil.centerContentInCell(new WritableCellFormat(fontHeader1)));
        for (CategorieOeuf categorieOeuf : findAll()) {
            ExcelUtil.addLabel(sheet, 0, startRowAt, categorieOeuf.getDesignation(), cellFormatCategorie);
            startRowAt = addNamesOfAttributs(categorieOeuf, sheet, startRowAt + 1, new WritableCellFormat(fontAttributs));
        }
        ExcelUtil.addLabel(sheet, 0, sheet.getRows() + 1, "Total des pertes", totalPertes);
    }

    public static void applicateMegesOnTheSheet(WritableSheet sheet) throws WriteException {
        ExcelUtil.mergeRows(sheet, 0, 0, 2, 20);
        ExcelUtil.mergeRows(sheet, 0, 3, 4, 20);
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
        ExcelUtil.addLabel(sheet, 0, startRow, "SI", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 2, "Ventes", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 3, "Don", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 4, "Perte", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 5, "Démarrage", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 6, "SF", cellFormat);
    }

    private static void attributsOfOAC(WritableSheet sheet, int startRow, WritableCellFormat cellFormat) throws WriteException {
        ExcelUtil.addLabel(sheet, 0, startRow, "SI", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 1, "Entrés", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 2, "Mise en incubation", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 3, "Ventes", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 4, "Don", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 5, "Perte", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 6, "Démarrage", cellFormat);
        ExcelUtil.addLabel(sheet, 0, startRow + 7, "SF", cellFormat);
    }

}
