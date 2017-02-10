package com.xtr.comm.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.util.Collection;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/12 10:30
 */
public class ExcelDoc<T> {

    private String templateHead;

    private int startRow = 0;

    private String sheetName;

    private String outPath;

    private String[] headers;

    private String pattern;

    private Collection<T> dateSet;


    /* styles */
    private HSSFCellStyle headerStyle;

    private HSSFCellStyle rowCellStyle;

    private Short rowHeight;

    public ExcelDoc() {

    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public String getTemplateHead() {
        return templateHead;
    }

    public void setTemplateHead(String templateHead) {
        this.templateHead = templateHead;
    }

    public ExcelDoc(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public Collection<T> getDateSet() {
        return dateSet;
    }

    public void setDateSet(Collection<T> dateSet) {
        this.dateSet = dateSet;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public HSSFCellStyle getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(HSSFCellStyle headerStyle) {
        this.headerStyle = headerStyle;
    }

    public HSSFCellStyle getRowCellStyle() {
        return rowCellStyle;
    }

    public void setRowCellStyle(HSSFCellStyle rowCellStyle) {
        this.rowCellStyle = rowCellStyle;
    }

    public Short getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Short rowHeight) {
        this.rowHeight = rowHeight;
    }
}
