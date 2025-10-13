package com.bankapp.strategy;

import com.bankapp.entity.Account;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PdfReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(Account account) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(50, 700);
            content.showText("Account Report for " + account.getAccountNumber());
            content.newLineAtOffset(0, -20);
            content.showText("Balance: " + account.getBalance());
            content.endText();

            File file = new File("report_" + account.getAccountNumber() + ".pdf");
            document.save(file);
            System.out.println("PDF report generated: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
