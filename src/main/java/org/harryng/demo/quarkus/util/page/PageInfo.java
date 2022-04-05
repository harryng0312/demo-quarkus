package org.harryng.demo.quarkus.util.page;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private int pageNumber = 1;
    private int pageSize = 1;
    private int offset = 0;

    private Sort sort = Sort.UNSORTED;

    public PageInfo(int pageNumber, int pageSize, int offset, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.offset = offset;
        this.sort = sort;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
