package com.xtr.api.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


/***
 * Excel 读取工具类，暂时只支持excel2003
 *
 * @author zhangfeng
 * @date 2015-07-21
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);


    /***
     * 读取excel中的数据 返回一个list
     *
     * @param multipartFile
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel(MultipartFile multipartFile, int startline, int sheetNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        try {
            // 先用excel2003的方法 读取数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel03(multipartFile.getInputStream(), startline, sheetNum);
        } catch (Exception e) {
            // 如果出现异常， 则用excel2007的方法 读取数据
            // 暂时无法实现，容易出现错误数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel07(multipartFile.getInputStream(), startline, sheetNum);
        }
        return arraylist;
    }


    /**
     * 读取excel中的数据 返回一个list
     *
     * @param bytes
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel(byte[] bytes, int startline, int sheetNum) throws Exception {
        ArrayList<Object[]> arraylist = null;

        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            // 先用excel2003的方法 读取数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel03(inputStream, startline, sheetNum);
        } catch (Exception e) {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            // 如果出现异常， 则用excel2007的方法 读取数据
            // 暂时无法实现，容易出现错误数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel07(inputStream, startline, sheetNum);
        }
        return arraylist;
    }


    /**
     * 读取excel数据 适用excel2003
     *
     * @param inputstream
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel03(InputStream inputstream, int startline, int sheetNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        HSSFWorkbook hssfworkbook = new HSSFWorkbook(inputstream);
        HSSFSheet hssfsheet = hssfworkbook.getSheetAt(sheetNum);
        //总行数
        int trLength = hssfsheet.getPhysicalNumberOfRows();
        for (int j = startline; j < trLength; j++) {
            HSSFRow hssfrow = hssfsheet.getRow(j);
            if (hssfrow == null || isBlankRow(hssfrow)) {
                break;
            }
            short word0 = hssfrow.getLastCellNum();
            if (word0 >= 0) {
                Object[] aobj = new Object[word0];
                for (int k = 0; k < word0; k++) {
                    HSSFCell hssfcell = hssfrow.getCell((short) k);
                    if (hssfcell != null) {
                        aobj[k] = getValue(hssfcell);
                    }
                }
                arraylist.add(aobj);
            }
        }
        return arraylist;
    }


    /**
     * 读取excel数据 适用excel2007
     *
     * @param inputstream
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel07(InputStream inputstream, int startline, int sheetNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        Workbook wb = WorkbookFactory.create(inputstream);

        //3.得到Excel工作表对象
        Sheet sheet = wb.getSheetAt(sheetNum);
        //总行数
        int trLength = sheet.getLastRowNum() + 1;
        //4.得到Excel工作表的行
//        Row row = sheet.getRow(0);
        //总列数
//        int tdLength = row.getLastCellNum();
        for (int i = startline; i < trLength; i++) {
            //得到Excel工作表的行
            Row row1 = sheet.getRow(i);
            int tdLength = row1.getLastCellNum();
            if (row1 != null && tdLength > 0) {
                Object[] aobj = new Object[tdLength];
                for (int j = 0; j < tdLength; j++) {
                    //得到Excel工作表指定行的单元格
                    Cell cell1 = row1.getCell(j);
                    if (cell1 == null) {
                        break;
                    }
                    aobj[j] = getValue(cell1);
                }
                arraylist.add(aobj);
            }
        }
        return arraylist;
    }


    /**
     * 读取excel数据 适用excel2007
     *
     * @param inputstream
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel07InculdeBlankCell(InputStream inputstream, int startline, int sheetNum, int minCell) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        Workbook wb = WorkbookFactory.create(inputstream);

        //3.得到Excel工作表对象
        Sheet sheet = wb.getSheetAt(sheetNum);
        //总行数
        int trLength = sheet.getLastRowNum() + 1;
        //4.得到Excel工作表的行
//        Row row = sheet.getRow(0);
        //总列数
//        int tdLength = row.getLastCellNum();
        for (int i = startline; i < trLength; i++) {
            //得到Excel工作表的行
            Row row1 = sheet.getRow(i);
            int tdLength = row1.getLastCellNum();
            if (tdLength < minCell) {
                tdLength = minCell;
            }
            if (row1 != null && tdLength > 0) {
                Object[] aobj = new Object[tdLength];
                for (int j = 0; j < tdLength; j++) {
                    //得到Excel工作表指定行的单元格
                    Cell cell1 = row1.getCell(j);
                    if (cell1 == null) {
                        aobj[j] = null;
                    } else {
                        aobj[j] = getValue(cell1);
                    }
                }
                if (isBlankRow(aobj)) {
                    break;
                }
                arraylist.add(aobj);
            }
        }
        return arraylist;
    }

    /***
     * 判断行是否为空
     *
     * @param row
     * @return
     */
    public static boolean isBlankRow(HSSFRow row) {
        if (row == null) return true;
        boolean result = true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            HSSFCell cell = row.getCell(i, HSSFRow.RETURN_BLANK_AS_NULL);
            String value = "";
            if (cell != null) {
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        value = String.valueOf((int) cell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        value = String.valueOf(cell.getCellFormula());
                        break;
                    //case Cell.CELL_TYPE_BLANK:
                    //    break;
                    default:
                        break;
                }

                if (!value.trim().equals("")) {
                    result = false;
                    break;
                }
            }
        }

        return result;
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
                double d = hssfcell.getNumericCellValue();
                if (HSSFDateUtil.isCellDateFormatted(hssfcell)) {
                    value = com.xtr.comm.util.DateUtil.formatDate(new Date(HSSFDateUtil.getJavaDate(d).getTime()));
                    break;
                }
                int d_int = (int) d;
                if (d_int == d) {
                    value = BigDecimal.valueOf(d_int).toString();
                    break;
                }
                value = BigDecimal.valueOf(d).toPlainString();
                break;
            case HSSFCell.CELL_TYPE_STRING://字符串
                value = hssfcell.getStringCellValue().trim();
                break;
            case HSSFCell.CELL_TYPE_FORMULA://公式型
                FormulaEvaluator evaluator = hssfcell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(hssfcell);
                CellValue cellValue = evaluator.evaluate(hssfcell);
                switch (cellValue.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC://数值型
                        value = cellValue.getNumberValue();
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


    public static String getValue(Object[] row, int colNum) {
        if (row.length <= colNum) {
            return "";
        }
        return StringUtils.trimToEmpty(String.valueOf(row[colNum]));
    }

    public static String getValue(Object[] row, int colNum, String defaultValue) {
        String val = getValue(row, colNum);
        if (StringUtils.isEmpty(val)) {
            return defaultValue;
        }
        return val;
    }


    /**
     * 数据转换
     *
     * @param list
     * @param startLine
     * @param cellNumber
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> analyzeData(List<Object[]> list, int startLine, int cellNumber) throws Exception {
        //循环读取  封装对象
        int i = 0;
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        try {
            for (i = 0; i < list.size(); i++) {
                Object[] rowObj = list.get(i);

                Map<String, String> dataMap = new HashMap<String, String>();
                for (int k = 0; k < cellNumber; k++) {
                    String key = null;
                    if (k > 25) {
                        key = "A" + String.valueOf((char) (k - 26 + 65));
                    } else {
                        key = String.valueOf((char) (k + 65));
                    }
                    dataMap.put(key, ExcelUtil.getValue(rowObj, k));
                }
                dataList.add(dataMap);
            }
        } catch (Exception e) {
            throw new Exception("文件解析失败，请确认文件格式是否正确！\n错误行数【" + (i + startLine) + "】," + e.getMessage(), e);
        }
        return dataList;
    }


    /**
     * 获取文件上传路径
     *
     * @param suffix
     * @param excelPath
     * @return
     * @throws IOException
     */
    public static String getFilePath(String suffix, String excelPath) throws IOException {
//        String realPath = request.getSession().getServletContext().getRealPath(excelPath);
        String[] paths = excelPath.split("\\\\");
        StringBuffer fullPath = new StringBuffer();

        int length = paths.length;
        for (int i = 0; i < length; i++) {
            fullPath.append(paths[i]).append("\\");
            if (i > length - 3) {
                File file = new File(fullPath.toString());
                if (!file.exists()) {
                    file.mkdir();
                    LOGGER.info("创建目录为：" + fullPath.toString());
                }
            }
        }
        return excelPath + "/" + UUID.randomUUID() + suffix;
    }

    public static boolean isBlankRow(Object[] cols) {
        if (cols == null || cols.length == 0) {
            return true;
        }

        boolean isBlank = true;
        for (Object col : cols) {
            if (col == null)
                continue;
            if (!StringUtils.isBlank(col + "")) {
                isBlank = false;
                break;
            }
        }

        return isBlank;
    }
}
