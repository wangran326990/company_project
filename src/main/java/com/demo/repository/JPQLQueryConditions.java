package com.demo.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Data
@Builder
@AllArgsConstructor
public class JPQLQueryConditions {
    private final String jpql;
    private final Map<String, Object> parameters;
}
