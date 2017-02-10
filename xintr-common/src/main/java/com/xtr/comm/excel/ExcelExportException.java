package com.xtr.comm.excel;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/12 10:38
 */
public class ExcelExportException extends RuntimeException {

    public ExcelExportException() {
    }

    public ExcelExportException(String message) {
        super(message);
    }

    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelExportException(Throwable cause) {
        super(cause);
    }

    public ExcelExportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
