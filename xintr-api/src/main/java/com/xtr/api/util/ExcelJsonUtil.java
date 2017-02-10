package com.xtr.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2016/8/8.
 */
public class ExcelJsonUtil {

    /**
     * 读取excel数据
     *
     * @param bytes
     */
    public static List<Map<Integer, Object>> readExcelToObj(byte[] bytes) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        List<Map<Integer, Object>> list = null;
        Workbook wb = null;

        try {
            wb = WorkbookFactory.create(in);
            list = readExcel07(wb, 0, 2, 0);

        } catch (IOException e) {
            e.printStackTrace();
            HSSFWorkbook hs = new HSSFWorkbook(in);
            list = readExcel03(hs, 0, 2, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;


    }

    /**
     * 97-03 excel的版本解析
     *
     * @param wb
     * @param sheetIndex
     * @param startReadLine
     * @param tailLine
     * @return
     */
    private static List readExcel03(HSSFWorkbook wb, int sheetIndex, int startReadLine, int tailLine) {

        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = null;
        Map<Integer, Object> map = null;
        List list = new ArrayList();
        int cellcounts = 0;
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {
            row = sheet.getRow(i);
            if (null == row || row.getCell(0) == null || "".equals(row.getCell(0))) {
                break;
            }
            if (cellcounts == 0)
                cellcounts = row.getPhysicalNumberOfCells();

            //默认从第6列开始
            map = new LinkedHashMap<Integer, Object>();
            for (int j = 0; j < cellcounts; j++) {

                boolean isMerge = isMergedRegion(sheet, i, row.getCell(j)
                        .getColumnIndex());
                // 判断是否具有合并单元格
                if (isMerge) {
                    String rs = getMergedRegionValue(sheet, row.getRowNum(),
                            row.getCell(j).getColumnIndex());
                    //将值加入到map中
                    map.put(j, rs);


                } else {
                    map.put(j, getValue(row.getCell(j)));

                }

            }
            list.add(map);

        }
        return list;
    }

    /**
     * 读取excel文件
     *
     * @param wb
     * @param sheetIndex    sheet页下标：从0开始
     * @param startReadLine 开始读取的行：从第3行开始读
     * @param tailLine      去除最后读取的行
     */
    private static List readExcel07(Workbook wb, int sheetIndex, int startReadLine,
                                    int tailLine) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = null;
        Map<Integer, Object> map = null;
        List list = new ArrayList();
        int cellcounts = 0;
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {

            row = sheet.getRow(i);

            if (null == row || row.getCell(0) == null || "".equals(row.getCell(0))) {
                break;
            }
            if (cellcounts == 0)
                cellcounts = row.getPhysicalNumberOfCells();

            //默认从第6列开始
            map = new LinkedHashMap<Integer, Object>();
            for (int j = 0; j < cellcounts; j++) {
                if (row.getCell(j) != null) {
                    boolean isMerge = isMergedRegion(sheet, i, row.getCell(j)
                            .getColumnIndex());
                    // 判断是否具有合并单元格
                    if (isMerge) {
                        String rs = getMergedRegionValue(sheet, row.getRowNum(),
                                row.getCell(j).getColumnIndex());
                        //将值加入到map中
                        map.put(j, rs);


                    } else {
                        map.put(j, getValue(row.getCell(j)));

                    }
                }

            }
            list.add(map);

        }
        return list;


    }

    /**
     * 获取单元格的值
     *
     * @param hssfcell
     * @return
     */
    public static Object getValue(Cell hssfcell) {
        Object value = null;
        switch (hssfcell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC://数值型
//                double d = hssfcell.getNumericCellValue();
//                if (HSSFDateUtil.isCellDateFormatted(hssfcell)) {
//                    value = com.xtr.comm.util.DateUtil.formatDate(new Date(HSSFDateUtil.getJavaDate(d).getTime()));
//                    break;
//                }
//                int d_int = (int) d;
//                if (d_int == d) {
//                    value = BigDecimal.valueOf(d_int).toString();
//                    break;
//                }

//                double d = hssfcell.getNumericCellValue();
//                BigDecimal   b   =   new   BigDecimal(d);
//                value =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                DecimalFormat df = new DecimalFormat("0.00");
                double d = hssfcell.getNumericCellValue();
                value = df.format(d);
//                if(hssfcell!=null||"".equals(hssfcell.getNumericCellValue())){
//                    value =  df.format(hssfcell.getNumericCellValue());
//                }else{
//                    value="";
//                }
//                DecimalFormat df = new DecimalFormat("0.##");
//                value =  df.format(hssfcell. getNumericCellValue());

                break;
            case HSSFCell.CELL_TYPE_STRING://字符串
                value = hssfcell.getStringCellValue().trim().replaceAll("\n", "");
                break;
            case HSSFCell.CELL_TYPE_FORMULA://公式型
                FormulaEvaluator evaluator = hssfcell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(hssfcell);
                CellValue cellValue = evaluator.evaluate(hssfcell);
                switch (cellValue.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC://数值型

//                        double d1 = hssfcell.getNumericCellValue();
//                        BigDecimal   b1   =   new   BigDecimal(d1);
//                        value =   b1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        double d1 = hssfcell.getNumericCellValue();
                        value = df1.format(d1);
//                        double d1 = hssfcell.getNumericCellValue();
//                        if (HSSFDateUtil.isCellDateFormatted(hssfcell)) {
//                            value = com.xtr.comm.util.DateUtil.formatDate(new Date(HSSFDateUtil.getJavaDate(d1).getTime()));
//                            break;
//                        }
//                        int d_int1 = (int) d1;
//                        if (d_int1 == d1) {
//                            value = BigDecimal.valueOf(d_int1).toString();
//                            break;
//                        }
//                        value = BigDecimal.valueOf(d1).toPlainString();
                        break;
                    case HSSFCell.CELL_TYPE_STRING://字符串
                        value = String.valueOf(cellValue.getStringValue()).trim();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK://空的单元格
                        value = "";
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN://布尔值
                        value = new Boolean(cellValue.getBooleanValue());
                        break;
                    default:
                        value = "";
                        break;
                }
                break;
            case HSSFCell.CELL_TYPE_BLANK://空的单元格
                value = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = new Boolean(hssfcell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = new Byte(hssfcell.getErrorCellValue());
                break;
            default:
                value = "";
                break;
        }
        return value;
    }


    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return (String) getValue(fCell);
                }
            }
        }

        return null;
    }

    /**
     * 判断合并了行
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedRow(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     *
     * @param sheet
     * @return
     */
    private boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {

        if (cell == null) return "";

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(XSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(XSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == XSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param fCell
     * @return
     */
    private static String getCellFormatValue(Cell fCell) {
        String cellvalue = "";
        if (fCell != null) {
            // 判断当前Cell的Type
            switch (fCell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(fCell)) {
                        Date date = fCell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(fCell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为string
                case XSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = fCell.getRichStringCellValue().getString().replace("\n", "");
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }


    /**
     * 返回json字符串格式
     *
     * @param
     * @param list
     * @return
     */
    public static String getJsonString(List<Map<Integer, Object>> listMap, List list) {

        if (!list.isEmpty()) {
            List list1 = new ArrayList();
            List list2 = new ArrayList();
            Map<Integer, Object> mapLine3 = listMap.get(0);//第三行所有的标题
            for (int a : mapLine3.keySet()) {
                if (a >= 6) {
                    list1.add(mapLine3.get(a));
                }
            }
            Map<Integer, Object> mapLine4 = listMap.get(1);//第四行的所有的标题
            for (int b : mapLine4.keySet()) {
                if (b >= 6) {
                    list2.add(mapLine4.get(b));
                }
            }
            //循环list

            //定义一个map来存放每个元素出现的次数
            Map<Object, Integer> elementsCount = new LinkedHashMap<Object, Integer>();
            for (Object s : list1) {
                Integer i = elementsCount.get(s);
                if (i == null) {
                    elementsCount.put(s, 1);
                } else {
                    elementsCount.put(s, i + 1);
                }
            }


            JSONObject jsonObject = null;
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArrayNew = null;
            Map jsonMap = null;
            int count = 0;
            int index = 0;
            int size = list.size();
            int size2 = list2.size();
            for (Object key : elementsCount.keySet()) {
                //获得当前的次数
                count = elementsCount.get(key);
                if (list2.contains(key)) {
                    jsonMap = new HashMap();
                    jsonArrayNew = new JSONArray();
                    jsonObject = new JSONObject();
                    if (key != null)
                        jsonMap.put(key.toString(), size > index ? list.get(index) : "");//动态赋值list
                    jsonArrayNew.add(jsonMap);
                    jsonObject.put(key.toString(), jsonArrayNew);
                    jsonArray.add(jsonObject);


                } else {

                    if (count >= 1) {
                        //当前元素下表 index -- index+count-1的下标

                        JSONArray jsonArrayNew2 = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        for (int j = index; j <= index + count - 1; j++) {
                            Map jsonMap1 = new HashMap();
                            jsonMap1.put(size2 > index ? list2.get(index).toString() : "", size > index ? list.get(index) : "");
                            jsonArrayNew2.add(jsonMap1);
                            count = count - 1;
                            index++;
                        }

                        jsonObject1.put(key.toString(), jsonArrayNew2);
                        jsonArray.add(jsonObject1);
                    }

                }

                if (count == 1) {
                    index++;
                }

            }
            return jsonArray.toJSONString();
        }

        return null;

    }

    /**
     * 根据byte字节数组获取当前excel文件的
     *
     * @param bytes
     * @return
     */
    public static Map getJsonList(byte[] bytes) throws Exception {
        List<Map<Integer, Object>> listMap = readExcelToObj(bytes);//每行数据都是map
        List list = null;
        List listJson = new ArrayList();
        Map<Integer, Object> mapList = new LinkedHashMap<Integer, Object>();
        Map map1 = new HashMap();
        //循环map
        int i = 0;
        for (Map<Integer, Object> map : listMap) {
            if (!(i == 0 || i == 1)) {
                list = new ArrayList();
                for (int a : map.keySet()) {
                    if (a >= 6) {
                        list.add(map.get(a));
                    }
                    mapList.put(i, list);
                }

            }

            i++;
        }
        for (int a : mapList.keySet()) {
            listJson.add(getJsonString(listMap, (List) mapList.get(a)));

        }


        map1.put("listMap", listMap);
        map1.put("listJson", listJson);
        return map1;

    }


    /**
     * 转换数据   将list的每一条数据  封装到map中
     *
     * @param dataMap
     * @param jsonList
     * @return
     */
    public static List<Map<Integer, Object>> translate(List<Map<Integer, Object>> dataMap, List jsonList) {
        List<Map<Integer, Object>> list = new ArrayList();
        //将list中的标题map去掉
        List<Map<Integer, Object>> listNew = new ArrayList();
        for (int i = 0; i < dataMap.size(); i++) {
            if (!(i == 0 || i == 1)) {
                listNew.add(dataMap.get(i));
            }
        }


        int i = 0;
        for (Map map : listNew) {
            map.put(100, jsonList.get(i));
            list.add(map);
            i++;
        }
        return list;
    }


    public static List<Map<Integer, Object>> getListMap(byte[] bytes) throws Exception {

        Map map = getJsonList(bytes);
        List<Map<Integer, Object>> dataMap = (List<Map<Integer, Object>>) map.get("listMap");
        List listJson = (List) map.get("listJson");
        List<Map<Integer, Object>> list = translate(dataMap, listJson);
        return list;
    }

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\admin\\Desktop\\11.xlsx";

        byte[] buffer = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();


            List list = getListMap(buffer);
            for (Object map : list) {
                System.out.println(map);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}








