package com.example.demo.net;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/net")
@RequiredArgsConstructor
public class NetController {
    private final NetCsvService csvService;
    private final JdbcTemplate jdbc;

    // 
    @PostMapping("/ingest/wireshark-csv")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        int count = csvService.ingestWiresharkCsv(file);
        return Map.of("ingested", count);
    }
}
