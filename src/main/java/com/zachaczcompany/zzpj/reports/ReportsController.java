package com.zachaczcompany.zzpj.reports;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/report")
public class ReportsController {
    ReportsGenerator reportsGenerator;

    public ReportsController(ReportsGenerator reportsGenerator) {
        this.reportsGenerator = reportsGenerator;
    }

    @GetMapping("/searchStatistics/{reportType}")
    public ResponseEntity<byte[]> getSearchStatistics(@PathVariable ReportTypes reportType, @RequestParam long id) {
        byte[] file = reportsGenerator.getSearchStatistics(reportType, id);
        String filename = "searchStatistics" + reportType.getExtension();

        HttpHeaders headers = getHttpHeaders(filename);
        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @GetMapping("/opinions/{reportType}")
    public ResponseEntity<byte[]> getOpinions(@PathVariable ReportTypes reportType, @RequestParam long id) {
        byte[] file = reportsGenerator.getOpinions(reportType, id);
        String filename = "opinions" + reportType.getExtension();

        HttpHeaders headers = getHttpHeaders(filename);
        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    private HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.setContentDispositionFormData(filename, filename);
        return headers;
    }
}
