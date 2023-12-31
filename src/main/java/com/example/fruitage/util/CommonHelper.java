package com.example.fruitage.util;

import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import java.util.List;

public class CommonHelper {

  private CommonHelper() {
  }

  // name,asc | name,desc
  public static Sort buildSortFromQuery(List<String> sortQuery) {
    Sort sort = Sort.empty();
    sortQuery.forEach(q -> {
      String[] s = q.split(",");
      sort.and(s[0], "asc".equals(s[1]) ? Direction.Ascending : Direction.Descending);
    });
    return sort;
  }
}
