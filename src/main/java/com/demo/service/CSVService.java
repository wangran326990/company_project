package com.demo.service;

import com.demo.dto.ExportResponse;
import com.demo.dto.TransactionReportDto;
import com.demo.dto.TransactionSearchRequestDto;
import com.demo.enums.TransactionColumnEnum;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CSVService {

    private static final int BATCH_SIZE = 1_000;
    private final ReportService reportService;
    private static final Path EXPORT_DIRECTORY =
            Paths.get(System.getProperty("java.io.tmpdir"), "exports");
    static {
        try {
            Files.createDirectories(EXPORT_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CSVService(ReportService reportService) {
        this.reportService = reportService;
    }

    public ExportResponse createExport(TransactionSearchRequestDto requestDto, String downloadId) {
        String fileName = "report-" + downloadId + ".csv";
        Path filePath = EXPORT_DIRECTORY.resolve(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            CSVPrinter csvPrinter = new CSVPrinter(
                    writer,
                    CSVFormat.DEFAULT.withHeader(
                            TransactionColumnEnum.ID.getLabel(),
                            TransactionColumnEnum.ACCOUNT_ID.getLabel(),
                            TransactionColumnEnum.DATE_TIME.getLabel(),
                            TransactionColumnEnum.TRAN_TYPE.getLabel(),
                            TransactionColumnEnum.PLATFORM_TRAN_ID.getLabel(),
                            TransactionColumnEnum.GAME_TRAN_ID.getLabel(),
                            TransactionColumnEnum.GAME_ID.getLabel(),
                            TransactionColumnEnum.AMOUNT.getLabel(),
                            TransactionColumnEnum.BALANCE.getLabel()
                    ));
            Long lastId = null;
            while(true) {
                List<TransactionReportDto> reports =
                        reportService.getExcelData(
                                requestDto,
                                lastId,
                                BATCH_SIZE);
                if (reports.isEmpty()) {
                    break;
                }
                lastId = reports.get(reports.size() - 1).getId();
                for (TransactionReportDto report : reports) {
                    csvPrinter.printRecord(
                            report.getId(),
                            report.getAccountId(),
                            report.getDateTime(),
                            report.getTranType(),
                            report.getPlatformTranId(),
                            report.getGameTranId(),
                            report.getGameId(),
                            report.getAmount(),
                            report.getBalance()
                    );
                }
                csvPrinter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new ExportResponse(
                downloadId,
                fileName,
                "/api/v1/report/export/" + downloadId + "/download");

    }

    public ResponseEntity<ResourceRegion> download(
            String exportId,
            HttpHeaders headers) throws IOException {

        Path file = getExportFile(exportId);

        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        long contentLength = resource.contentLength();

        List<HttpRange> ranges = headers.getRange();

        // Normal download
        if (ranges.isEmpty()) {

            ResourceRegion region =
                    new ResourceRegion(
                            resource,
                            0,
                            contentLength
                    );

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.ACCEPT_RANGES,
                            "bytes"
                    )
                    .contentType(
                            MediaType.parseMediaType("text/csv")
                    )
                    .contentLength(contentLength)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    file.getFileName() +
                                    "\""
                    )
                    .body(region);
        }

        // Resume download
        HttpRange range = ranges.get(0);

        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);

        long length = end - start + 1;

        ResourceRegion region =
                new ResourceRegion(
                        resource,
                        start,
                        length
                );

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header(
                        HttpHeaders.ACCEPT_RANGES,
                        "bytes"
                )
                .header(
                        HttpHeaders.CONTENT_RANGE,
                        "bytes " +
                                start +
                                "-" +
                                end +
                                "/" +
                                contentLength
                )
                .contentType(
                        MediaType.parseMediaType("text/csv")
                )
                .contentLength(length)
                .body(region);
    }

    private Path getExportFile(String exportId) {

        UUID uuid;

        try {
            uuid = UUID.fromString(exportId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(exportId);
        }

        Path file = EXPORT_DIRECTORY.resolve(
                "report-" + uuid + ".csv"
        );

        if (!Files.exists(file)) {
            throw new RuntimeException(exportId);
        }

        return file;
    }
}
