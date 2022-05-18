package org.harryng.demo.quarkus.util.page;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import java.io.Serializable;

public class PageInfo extends Page implements Serializable {

    private Sort sort = null;

    public PageInfo(int size) {
        super(size);
    }

    public PageInfo(int index, int size) {
        super(index, size);
    }

    public PageInfo(int index, int size, Sort sort){
        super(index, size);
        this.sort = sort;
    }

    public Sort getSort() {
        if(sort == null){
            sort = Sort.empty();
        }
        return sort;
    }
}
