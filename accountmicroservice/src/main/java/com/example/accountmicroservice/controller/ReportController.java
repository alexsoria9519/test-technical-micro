package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.models.ReportDTO;
import com.example.accountmicroservice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    private  final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<ReportDTO> getMovementsByDateClientName(
            @RequestParam String name,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return reportService.getMovementsByDateClientName(startDate, endDate, name);
    }


}
