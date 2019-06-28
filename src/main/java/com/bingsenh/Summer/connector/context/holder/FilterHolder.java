package com.bingsenh.Summer.connector.context.holder;

import com.bingsenh.Summer.filter.Filter;
import lombok.Data;

/**
 * Created by bingsenh on 2019/6/27.
 */
@Data
public class FilterHolder {
    private Filter filter;
    private String filterClass;

    public FilterHolder(String filterClass){
        this.filterClass = filterClass;
    }
}
