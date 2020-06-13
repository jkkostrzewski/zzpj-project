package com.zachaczcompany.zzpj.reports;

import com.itextpdf.text.DocumentException;
import io.vavr.collection.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportsGenerator {


    public byte[] getReport() throws DocumentException, IOException {
        List<String> of = io.vavr.collection.List.of("Elmpolee", "Conference", "Type");
        PdfCreator pdfCreator = new PdfCreator(of);
        pdfCreator
                .addRow()
                .cell(12)
                .cell("elo")
                .cell(12.32);
        byte[] statisticsFile2 = pdfCreator.getStatisticsFile();
//        OutputStream os = new FileOutputStream("file.pdf");
//        os.write(statisticsFile2);
        return statisticsFile2;
    }

    public byte[] getXslx() throws IOException {
        List<String> of = io.vavr.collection.List.of("Elmpolee", "Conference", "Type");
        SheetCreator sheetCreator = new SheetCreator("name", of);
        sheetCreator
                .addRow()
                .cell(12)
                .cell("elo")
                .cell(12.32);
        Workbook workbook = sheetCreator.getStatisticsFile();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        return stream.toByteArray();
    }

}
