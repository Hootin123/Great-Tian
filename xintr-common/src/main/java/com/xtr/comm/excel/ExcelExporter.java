package com.xtr.comm.excel;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/12 10:35
 */
public class ExcelExporter {

    private static Logger LG = LoggerFactory.getLogger(ExcelExporter.class);

    /**
     * 用来验证excel与Vo中的类型是否一致 <br>
     * Map<栏位类型,只能是哪些Cell类型>
     */
    private static Map<Class<?>, Integer[]> validateMap = new HashMap<Class<?>, Integer[]>();

    static {
        validateMap.put(String[].class, new Integer[]{Cell.CELL_TYPE_STRING});
        validateMap.put(Double[].class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(String.class, new Integer[]{Cell.CELL_TYPE_STRING});
        validateMap.put(Double.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Date.class, new Integer[]{Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_STRING});
        validateMap.put(Integer.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Float.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Long.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Boolean.class, new Integer[]{Cell.CELL_TYPE_BOOLEAN});
    }

    /**
     * 获取cell类型的文字描述
     *
     * @param cellType <pre>
     *                                 Cell.CELL_TYPE_BLANK
     *                                 Cell.CELL_TYPE_BOOLEAN
     *                                 Cell.CELL_TYPE_ERROR
     *                                 Cell.CELL_TYPE_FORMULA
     *                                 Cell.CELL_TYPE_NUMERIC
     *                                 Cell.CELL_TYPE_STRING
     *                                 </pre>
     * @return
     */
    private static String getCellTypeByInt(int cellType) {
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return "Null type";
            case Cell.CELL_TYPE_BOOLEAN:
                return "Boolean type";
            case Cell.CELL_TYPE_ERROR:
                return "Error type";
            case Cell.CELL_TYPE_FORMULA:
                return "Formula type";
            case Cell.CELL_TYPE_NUMERIC:
                return "Numeric type";
            case Cell.CELL_TYPE_STRING:
                return "String type";
            default:
                return "Unknown type";
        }
    }


    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    public static HSSFWorkbook workbook(ExcelDoc excelDoc) throws ExcelExportException {
        if (null == excelDoc) {
            throw new ExcelExportException("ExcelDoc not is null.");
        }

        HSSFWorkbook workbook = null;
        try {
            // 声明一个工作薄
            if (StringUtils.isNotBlank(excelDoc.getTemplateHead())) {
                workbook = new HSSFWorkbook(new FileInputStream(excelDoc.getTemplateHead()));
            } else {
                workbook = new HSSFWorkbook();
            }
            return workbook;
        } catch (FileNotFoundException e) {
            LG.error(e.getMessage(), e);
        } catch (IOException e) {
            LG.error(e.getMessage(), e);
        }
        return null;
    }

    public static void export(ExcelDoc excelDoc, HSSFWorkbook workbook) throws ExcelExportException {

        if (null == excelDoc) {
            throw new ExcelExportException("ExcelDoc not is null.");
        }

        String sheetName = excelDoc.getSheetName();

        OutputStream out = null;
        HSSFSheet sheet = null;
        try {
            out = new FileOutputStream(excelDoc.getOutPath());

            // 声明一个工作薄
            if (StringUtils.isNotBlank(excelDoc.getTemplateHead())) {
                sheet = workbook.getSheetAt(0);
                if (StringUtils.isNotBlank(sheetName)) {
                    workbook.setSheetName(0, sheetName);
                }
            } else {
                sheet = StringUtils.isNotBlank(sheetName) ? workbook.createSheet(sheetName) : workbook.createSheet();
            }

            write2Sheet(sheet, excelDoc);

            try {
                workbook.write(out);
            } catch (IOException e) {
                LG.error(e.toString(), e);
            }
        } catch (FileNotFoundException e) {
            LG.error(e.getMessage(), e);
        } catch (IOException e) {
            LG.error(e.getMessage(), e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void export(ExcelDoc excelDoc) throws ExcelExportException {

        if (null == excelDoc) {
            throw new ExcelExportException("ExcelDoc not is null.");
        }

        String sheetName = excelDoc.getSheetName();

        OutputStream out = null;
        HSSFWorkbook workbook = null;
        HSSFSheet sheet = null;
        try {
            out = new FileOutputStream(excelDoc.getOutPath());

            // 声明一个工作薄
            if (StringUtils.isNotBlank(excelDoc.getTemplateHead())) {
                workbook = new HSSFWorkbook(new FileInputStream(excelDoc.getTemplateHead()));
                sheet = workbook.getSheetAt(0);
                if (StringUtils.isNotBlank(sheetName)) {
                    workbook.setSheetName(0, sheetName);
                }
            } else {
                workbook = new HSSFWorkbook();
                sheet = StringUtils.isNotBlank(sheetName) ? workbook.createSheet(sheetName) : workbook.createSheet();
            }

            write2Sheet(sheet, excelDoc);
//            write2Sheet(sheet, headers, dataset, pattern, excelDoc.getStartRow());

            try {
                workbook.write(out);
            } catch (IOException e) {
                LG.error(e.toString(), e);
            }
        } catch (FileNotFoundException e) {
            LG.error(e.getMessage(), e);
        } catch (IOException e) {
            LG.error(e.getMessage(), e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> void write2Sheet(HSSFSheet sheet, ExcelDoc excelDoc) {
        int startRow = excelDoc.getStartRow();
        String[] headers = excelDoc.getHeaders();
        Collection dataset = excelDoc.getDateSet();
        String pattern = excelDoc.getPattern();

        if (null != headers && headers.length > 0) {
            // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                if (null != excelDoc.getHeaderStyle()) {
                    cell.setCellStyle(excelDoc.getHeaderStyle());
                }
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = startRow - 1;
        while (it.hasNext()) {
            index++;
            HSSFRow row = sheet.createRow(index);
            if (null != excelDoc.getRowHeight()) {
                row.setHeight(excelDoc.getRowHeight());
            }
            T t = it.next();
            try {
                if (t instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) t;
                    int cellNum = 0;
                    for (String k : headers) {
                        if (!map.containsKey(k)) {
                            LG.error("Map 中 不存在 key [" + k + "]");
                            continue;
                        }
                        Object value = map.get(k);
                        HSSFCell cell = row.createCell(cellNum);
                        if (null != excelDoc.getRowCellStyle()) {
                            cell.setCellStyle(excelDoc.getRowCellStyle());
                        }
                        cell.setCellValue(String.valueOf(value));
                        cellNum++;
                    }
                } else {
                    List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                    int cellNum = 0;
                    for (int i = 0; i < fields.size(); i++) {
                        HSSFCell cell = row.createCell(cellNum);
                        if (null != excelDoc.getRowCellStyle()) {
                            cell.setCellStyle(excelDoc.getRowCellStyle());
                        }
                        Field field = fields.get(i).getField();
                        field.setAccessible(true);
                        Object value = field.get(t);
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else if (value instanceof String[]) {
                            String[] strArr = (String[]) value;
                            for (int j = 0; j < strArr.length; j++) {
                                String str = strArr[j];
                                cell.setCellValue(str);
                                if (j != strArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else if (value instanceof Double[]) {
                            Double[] douArr = (Double[]) value;
                            for (int j = 0; j < douArr.length; j++) {
                                Double val = douArr[j];
                                // 资料不为空则set Value
                                if (null != val) {
                                    cell.setCellValue(val);
                                }

                                if (j != douArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            String empty = StringUtils.EMPTY;
                            ExcelCell anno = field.getAnnotation(ExcelCell.class);
                            if (anno != null) {
                                empty = anno.defaultValue();
                            }
                            textValue = value == null ? empty : value.toString();
                        }
                        if (textValue != null) {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }

                        cellNum++;
                    }
                }
            } catch (Exception e) {
                LG.error(e.toString(), e);
            }
        }

        if (null != headers) {
            // 设定自动宽度
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * 每个sheet的写入
     *
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     * @param pattern 日期格式
     */
    private static <T> void write2Sheet(HSSFSheet sheet, String[] headers, Collection<T> dataset,
                                        String pattern, int startRow) {
        if (startRow == 0) {
            // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = startRow - 1;
        while (it.hasNext()) {
            index++;
            HSSFRow row = sheet.createRow(index);
            T t = it.next();
            try {
                if (t instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) t;
                    int cellNum = 0;
                    for (String k : headers) {
                        if (!map.containsKey(k)) {
                            LG.error("Map 中 不存在 key [" + k + "]");
                            continue;
                        }
                        Object value = map.get(k);
                        HSSFCell cell = row.createCell(cellNum);
                        cell.setCellValue(String.valueOf(value));
                        cellNum++;
                    }
                } else {
                    List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                    int cellNum = 0;
                    for (int i = 0; i < fields.size(); i++) {
                        HSSFCell cell = row.createCell(cellNum);
                        Field field = fields.get(i).getField();
                        field.setAccessible(true);
                        Object value = field.get(t);
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else if (value instanceof String[]) {
                            String[] strArr = (String[]) value;
                            for (int j = 0; j < strArr.length; j++) {
                                String str = strArr[j];
                                cell.setCellValue(str);
                                if (j != strArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else if (value instanceof Double[]) {
                            Double[] douArr = (Double[]) value;
                            for (int j = 0; j < douArr.length; j++) {
                                Double val = douArr[j];
                                // 资料不为空则set Value
                                if (null != val) {
                                    cell.setCellValue(val);
                                }

                                if (j != douArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            String empty = StringUtils.EMPTY;
                            ExcelCell anno = field.getAnnotation(ExcelCell.class);
                            if (anno != null) {
                                empty = anno.defaultValue();
                            }
                            textValue = value == null ? empty : value.toString();
                        }
                        if (textValue != null) {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }

                        cellNum++;
                    }
                }
            } catch (Exception e) {
                LG.error(e.toString(), e);
            }
        }

        if (null != headers) {
            // 设定自动宽度
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * 根据annotation的seq排序后的栏位
     *
     * @param clazz
     * @return
     */
    private static List<FieldForSortting> sortFieldByAnno(Class<?> clazz) {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<FieldForSortting> fields = new ArrayList<FieldForSortting>();
        List<FieldForSortting> annoNullFields = new ArrayList<FieldForSortting>();
        for (Field field : fieldsArr) {
            ExcelCell ec = field.getAnnotation(ExcelCell.class);
            if (ec == null) {
                // 没有ExcelCell Annotation 视为不汇入
                continue;
            }
            int id = ec.index();
            fields.add(new FieldForSortting(field, id));
        }
        fields.addAll(annoNullFields);
        //sortByProperties(fields, true, false, "index");
        return fields;
    }

    /*private static void sortByProperties(List<? extends Object> list, boolean isNullHigh,
                                         boolean isReversed, String... props) {
        if (CollectionUtils.isNotEmpty(list)) {
            Comparator<?> typeComp = ComparableComparator.getInstance();
            if (isNullHigh == true) {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            } else {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed) {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }

            List<Object> sortCols = new ArrayList<Object>();

            if (props != null) {
                for (String prop : props) {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0) {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }*/

}
