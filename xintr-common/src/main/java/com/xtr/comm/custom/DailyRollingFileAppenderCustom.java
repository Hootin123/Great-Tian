package com.xtr.comm.custom;


import org.apache.log4j.DailyRollingFileAppender;

import java.io.File;

public class DailyRollingFileAppenderCustom extends DailyRollingFileAppender {
    @Override
    public void setFile(String file) {
        String filePath = file;
        File fileCheck = new File(filePath);
        if (!fileCheck.exists())
            fileCheck.getParentFile().mkdirs();
        super.setFile(filePath);
    }
}
