package com.xtr.company.web;


import com.xtr.api.basic.ResultResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.io.OutputStream;

public class DistinctJacksonHttpMessageConverter extends
        MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        if (object != null && object instanceof ResultResponse) {

            OutputStream oStream = outputMessage.getBody();
            try {
                if (object instanceof ResultResponse) {
                    ResultResponse jr = (ResultResponse) object;
                    String text = this.getObjectMapper().writeValueAsString(jr);
                    if (StringUtils.isNotBlank(jr.getJsonpCallBack())) {
                        text = jr.getJsonpCallBack() + "(" + text + ")";
                    }

                    oStream.write(text.getBytes(DEFAULT_CHARSET));
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                ResultResponse jr = new ResultResponse();
                jr.setMessage("");
                jr.setSuccess(false);
                this.getObjectMapper().writeValue(oStream, jr);
            } finally {
                try {
                    oStream.flush();
                    oStream.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } else {
            // TODO Auto-generated method stub
            super.writeInternal(object, outputMessage);
        }
    }


}