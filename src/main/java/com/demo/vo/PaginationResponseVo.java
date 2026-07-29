package com.demo.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponseVo<T> {

    private List<T> data;

    private int currentPage;

    private int pageSize;

    private long totalRecords;

    private int totalPages;


    public boolean hasPrevious(){
        return currentPage > 1;
    }


    public boolean hasNext(){
        return currentPage < totalPages;
    }
}