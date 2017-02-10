package com.xtr.export.excel;

import com.xtr.comm.excel.ExcelDoc;
import com.xtr.comm.excel.ExcelExporter;
import com.xtr.comm.excel.WorkModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/12 10:30
 */
public class TestMain {

    public static void main(String[] args) {

        List<WorkModel> dataset = new ArrayList<WorkModel>();
        dataset.add(new WorkModel("1", "6214830216461367", "招商银行", "李昭君", "116.00", "对私", "借记卡"));
        dataset.add(new WorkModel("2", "6214830230814724", "招商银行", "王量", "98.00", "对私", "借记卡"));
        dataset.add(new WorkModel("3", "6214832129836651", "招商银行", "许明", "105.00", "对私", "借记卡"));
        dataset.add(new WorkModel("4", "6214852104734762", "招商银行", "陈婉莹", "128.56", "对私", "借记卡"));
        dataset.add(new WorkModel("5", "6214832132380911", "招商银行", "李硕云", "105.00", "对私", "借记卡"));
        dataset.add(new WorkModel("6", "6217933300139217", "浦发银行", "周雅月", "105.00", "对私", "借记卡"));
        dataset.add(new WorkModel("7", "6227001218240061234", "建设银行", "孙玉霞", "125.00", "对私", "借记卡"));
        dataset.add(new WorkModel("8", "6214832132632089", "招商银行", "汪莹", "85.00", "对私", "借记卡"));
        dataset.add(new WorkModel("9", "6214851218486335", "招商银行", "赵倩慧", "85.00", "对私", "借记卡"));
        dataset.add(new WorkModel("10", "6214832132235529", "招商银行", "王丽霞", "81.00", "对私", "借记卡"));
        dataset.add(new WorkModel("11", "6214852106075933", "招商银行", "王惠颖", "85.00", "对私", "借记卡"));
        dataset.add(new WorkModel("12", "6225885866773963", "招商银行", "裴宁晖", "105.00", "对私", "借记卡"));
        dataset.add(new WorkModel("13", "6226090217136858", "招商银行", "宋俊", "86.00", "对私", "借记卡"));
        dataset.add(new WorkModel("14", "6214832132476156", "招商银行", "赵心田", "85.00", "对私", "借记卡"));
        dataset.add(new WorkModel("15", "6214832132787933", "招商银行", "陈思茹", "103.00", "对私", "借记卡"));
        dataset.add(new WorkModel("16", "6214831217417210", "招商银行", "郭大群", "75.00", "对私", "借记卡"));
        dataset.add(new WorkModel("17", "6214832132559266", "招商银行", "赵一敏", "96.00", "对私", "借记卡"));
        dataset.add(new WorkModel("18", "6214851216479415", "招商银行", "刘昱迎", "60.00", "对私", "借记卡"));
        dataset.add(new WorkModel("19", "6214831214484492", "招商银行", "何成彪", "55.00", "对私", "借记卡"));

        ExcelDoc excelDoc = new ExcelDoc("薪太软（上海）科技发展有限公司_2016年5月员工工资单");
        excelDoc.setTemplateHead("d:/template.xls");
        excelDoc.setStartRow(1);
        excelDoc.setDateSet(dataset);
        excelDoc.setOutPath("d:/work4.xls");

        HSSFWorkbook workbook = ExcelExporter.workbook(excelDoc);
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints( (short)10 );
        cellStyle.setFont(font);

        excelDoc.setRowCellStyle(cellStyle);
        excelDoc.setRowHeight( (short)450 );

        ExcelExporter.export(excelDoc, workbook);

    }

}
