package com.expense.app.service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.repository.ExpenseRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfGenerationService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public byte[] generateExpenseReportForUser(String userId) throws IOException {
        // Fetch expenses for the specific user
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(userId);

        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for writing to the PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Title
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Expense Report for User: " + userId);
                contentStream.endText();

                // Column headers
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Date");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Description");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText("Amount");
                contentStream.endText();

                // Content
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                int yPosition = 680;
                for (ExpenseEntity expense : expenses) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(expense.getDate().toString());
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(expense.getDescription());
                    contentStream.newLineAtOffset(300, 0);
                    contentStream.showText(expense.getAmount().toString());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Category-wise amount spent
                Map<String, Double> categoryAmounts = expenses.stream()
                        .collect(Collectors.groupingBy(
                                expense -> expense.getCategory() != null ? expense.getCategory().getName() : "Unknown",
                                Collectors.summingDouble(expense -> expense.getAmount().doubleValue())
                        ));

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Category-wise Amount Spent");
                contentStream.endText();

                yPosition -= 30;
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Category");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Amount");
                contentStream.endText();

                yPosition -= 20;
                for (Map.Entry<String, Double> entry : categoryAmounts.entrySet()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(entry.getKey());
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(entry.getValue().toString());
                    contentStream.endText();
                    yPosition -= 20;
                }

                // Expense Analysis
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Expense Analysis");
                contentStream.endText();

                yPosition -= 30;
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Total Expenses: " + expenses.stream()
                        .map(ExpenseEntity::getAmount)
                        .mapToDouble(Float::doubleValue)
                        .sum());
                contentStream.endText();
            }

            // Write the document to a byte array output stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
