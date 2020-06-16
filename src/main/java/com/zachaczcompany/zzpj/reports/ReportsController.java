package com.zachaczcompany.zzpj.reports;

import io.vavr.control.Either;
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

    @GetMapping("/searchStatistics/{id}")
    public ResponseEntity<byte[]> getSearchStatistics(@RequestParam ReportTypes reportType, @PathVariable long id) {
        Either<String, byte[]> searchStatistics = reportsGenerator.getSearchStatistics(reportType, id);
        return searchStatistics.fold(e -> new ResponseEntity<>(HttpStatus.NOT_FOUND),
                e -> getResponseFile("opinions", reportType, e));
    }

    @GetMapping("/opinions/{id}")
    public ResponseEntity<byte[]> getOpinions(@RequestParam ReportTypes reportType, @PathVariable long id) {
        Either<String, byte[]> opinions = reportsGenerator.getOpinions(reportType, id);
        return opinions.fold(e -> new ResponseEntity<>(HttpStatus.NOT_FOUND),
                e -> getResponseFile("opinions", reportType, e));
    }

    private ResponseEntity<byte[]> getResponseFile(String name, ReportTypes reportType, byte[] opinions) {
        String filename = name + reportType.getExtension();
        HttpHeaders headers = getHttpHeaders(filename);
        return new ResponseEntity<>(opinions, headers, HttpStatus.OK);
    }

    private HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.setContentDispositionFormData(filename, filename);
        return headers;
    }
}
