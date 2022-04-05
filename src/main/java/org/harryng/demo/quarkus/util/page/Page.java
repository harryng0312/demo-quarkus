package org.harryng.demo.quarkus.util.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

    private long total = 0L;
    private List<T> content = null;

    public Page(List<T> content, PageInfo pageInfo, long total) {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getContent() {
        if(content == null){
            content = new ArrayList<>();
        }
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
