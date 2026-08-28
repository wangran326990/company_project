package com.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class ExportResponse {
    private final String exportId;
    private final String fileName;
    private final String downloadUrl;
}
