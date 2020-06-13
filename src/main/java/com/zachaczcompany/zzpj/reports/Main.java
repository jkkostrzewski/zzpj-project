package com.zachaczcompany.zzpj.reports;

import com.itextpdf.text.DocumentException;
import io.vavr.collection.List;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) throws DocumentException, IOException {
        List<String> of = io.vavr.collection.List.of("Elmpolee", "Conference", "Type");
        PdfCreator pdfCreator = new PdfCreator(of);
        pdfCreator
                .addRow()
                .cell(12)
                .cell("elo")
                .cell(12.32);
        byte[] statisticsFile2 = pdfCreator.getStatisticsFile();
        OutputStream os = new FileOutputStream("file.pdf");
        os.write(statisticsFile2);
    }

//        public static void main2(String args[]) throws Exception {
//            // Creating a PdfDocument object
//            String dest = "C:/itextExamples/addingTable.pdf";
//            PdfWriter writer = new PdfWriter(dest);
//
//            // Creating a PdfDocument object
//            PdfDocument pdf = new PdfDocument(writer);
//
//            // Creating a Document object
//            Document doc = new Document(pdf);
//
//            // Creating a table
//            float [] pointColumnWidths = {150F, 150F, 150F};
//            Table table = new Table(pointColumnWidths);
//
//            // Adding cells to the table
//            table.addCell(new Cell().add("Name"));
//            table.addCell(new Cell().add("Raju"));
//            table.addCell(new Cell().add("Id"));
//            table.addCell(new Cell().add("1001"));
//            table.addCell(new Cell().add("Designation"));
//            table.addCell(new Cell().add("Programmer"));
//
//            // Adding Table to document
//            doc.add(table);
//
//            // Closing the document
//            doc.close();
//            System.out.println("Table created successfully..");
//        }
//    }
}
