package com.bankapp.strategy;

import org.springframework.stereotype.Component;

@Component
public class ReportStrategyFactory {

    private final PdfReportStrategy pdfStrategy;
    private final CsvReportStrategy csvStrategy;
    private final JsonReportStrategy jsonStrategy;

    public ReportStrategyFactory(PdfReportStrategy pdfStrategy,
                                 CsvReportStrategy csvStrategy,
                                 JsonReportStrategy jsonStrategy) {
        this.pdfStrategy = pdfStrategy;
        this.csvStrategy = csvStrategy;
        this.jsonStrategy = jsonStrategy;
    }

    public ReportStrategy getStrategy(String format) {
        switch (format.toUpperCase()) {
            case "PDF":
                return pdfStrategy;
            case "CSV":
                return csvStrategy;
            case "JSON":
                return jsonStrategy;
            default:
                throw new IllegalArgumentException("Unknown report format: " + format);
        }
    }
}
