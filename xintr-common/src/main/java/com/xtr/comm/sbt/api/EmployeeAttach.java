package com.xtr.comm.sbt.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 17:20.
 */
public class EmployeeAttach implements Serializable {

    private String id;
    private String name;
    private List<Attachment> attachment;

    public EmployeeAttach() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(Attachment... attachments) {
        this.attachment = Arrays.asList(attachments);
    }

}
