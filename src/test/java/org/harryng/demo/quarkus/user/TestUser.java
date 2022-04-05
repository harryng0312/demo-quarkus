package org.harryng.demo.quarkus.user;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class TestUser {
    @Test
    public void sort(){
        List<String> content = new ArrayList<>();
        PageInfo pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id"));
        Page p = new Page<>(content, pageInfo, 10);
        Log.info("unsorted: " + (Sort.unsorted()==null));
    }
}
