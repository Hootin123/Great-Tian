package com.github.miemiedev.mybatis.paginator.domain;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/26 15:43.
 */
public class Pager implements Serializable {

    private int offset;
    private int limit;

    public Pager(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
