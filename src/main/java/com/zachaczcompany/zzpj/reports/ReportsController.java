package com.zachaczcompany.zzpj.reports;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get search report for one shop", description = "Requires shop id in param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful report download"),
            @ApiResponse(responseCode = "404", description = "Shop with that id does not exist")
    })
    @GetMapping("/searchStatistics/{id}")
    public ResponseEntity<byte[]> getSearchStatistics(@RequestParam ReportTypes reportType, @PathVariable long id) {
        Either<String, byte[]> searchStatistics = reportsGenerator.getSearchStatistics(reportType, id);
        return searchStatistics.fold(e -> new ResponseEntity<>(HttpStatus.NOT_FOUND),
                e -> getResponseFile("opinions", reportType, e));
    }

    @Operation(summary = "Get opinion report for one shop", description = "Requires shop id in param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful shop statistics get"),
            @ApiResponse(responseCode = "404", description = "Shop with that id does not exist")
    })
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
