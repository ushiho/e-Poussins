/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author lotfi
 */
public class ExcelUtil {

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

    public static void mergeColumns(WritableSheet sheet, int row, int colStart, int colEnd, int sizeCell) throws WriteException {
        sheet.mergeCells(colStart, row, colEnd, row);
        sheet.setRowView(row, sizeCell);
    }

    public static void mergeRows(WritableSheet sheet, int col, int rowStart, int rowEnd, int sizeCell) throws WriteException {
        sheet.mergeCells(col, rowStart, col, rowEnd);
        sheet.setColumnView(col, sizeCell);
    }
}
