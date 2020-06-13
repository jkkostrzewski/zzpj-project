package com.zachaczcompany.zzpj.reports;

import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/report")
public class ReportsController {
    ReportsGenerator reportsGenerator;

    public ReportsController(ReportsGenerator reportsGenerator) {
        this.reportsGenerator = reportsGenerator;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getPdf() throws IOException, DocumentException {
        System.out.println("elo");
        byte[] file = reportsGenerator.getReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(file, headers, HttpStatus.OK);
        return response;
    }

    @GetMapping("/xslx")
    public ResponseEntity<byte[]> getXlsx() throws IOException, DocumentException {
        byte[] xslx = reportsGenerator.getXslx();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        String fileName = "exel.xlsx";
        header.setContentDispositionFormData(fileName, fileName);
        return new ResponseEntity<>(xslx,
                header, HttpStatus.CREATED);
    }
}
