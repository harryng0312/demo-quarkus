package org.harryng.demo.quarkus.util.page;

import io.quarkus.panache.common.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagedResult<T> implements Serializable {

    private long total = 0L;
    private List<T> content = null;
    private PageInfo pageInfo = null;

    public PagedResult(List<T> content, PageInfo pageInfo, long total) {
        this.total = total;
        this.content = content;
        this.pageInfo = pageInfo;
    }

    public long getTotal() {
        return total;
    }


    public List<T> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return content;
    }

    public PageInfo getPageInfo() {
        if(pageInfo == null){
            pageInfo = new PageInfo(0, 1, Sort.empty());
        }
        return pageInfo;
    }
}
