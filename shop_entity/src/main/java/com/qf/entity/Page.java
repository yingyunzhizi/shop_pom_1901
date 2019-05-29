package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @data 5/24/2019 0:34
 * @user yingyunzhizi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> implements Serializable {

    private int currentPage = 1;

    private int pageSize = 5;

    private int totalPage;

    private int totalCount;

    private List<T> list;

    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
