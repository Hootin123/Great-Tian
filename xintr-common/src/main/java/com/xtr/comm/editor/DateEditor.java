package com.xtr.comm.editor;

import com.xtr.comm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/27 18:54
 */
public class DateEditor extends PropertyEditorSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateEditor.class);

    private final DateFormat dateFormat;
    private final boolean allowEmpty;
    private final int exactDateLength;

    public DateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    public DateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            this.setValue(null);
        } else {
            if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
                throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
            }
            try {
                if (StringUtils.isEmpty(text))
                    this.setValue(null);
                else {
                    if (text.length() > 10){
                        this.setValue(DateUtil.string2Date(text));
                    }
                    else {
                        this.setValue(DateUtil.dateFormatter.parse(text));
                    }
                }
            } catch (ParseException var3) {
                throw new IllegalArgumentException("Could not parse date: " + var3.getMessage(), var3);
            }
        }
    }

    @Override
    public String getAsText() {
        Object obj = this.getValue();
        if (obj == null) {
            return "";
        } else {
            Date value = (Date) obj;
            return value != null ? this.dateFormat.format(value) : "";
        }
    }
}
