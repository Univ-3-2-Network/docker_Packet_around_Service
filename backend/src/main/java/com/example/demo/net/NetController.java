package com.example.demo.net;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

// Upload || Analyze
// API

@RestController
@RequestMapping("/api/net")
@RequiredArgsConstructor
public class NetController {
    private final NetCsvService csvService;
    private final JdbcTemplate jdbc;

    // API / Wireshark CSV upload 
    @PostMapping("/ingest/wireshark-csv")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        int count = csvService.ingestWiresharkCsv(file);
        return Map.of("ingested", count);
    }

    // 
    @GetMapping("/latest")
    public List<Map<String, Object>> dnsAnalysis(@RequestParam(defaultValue="3600") int seconds){
        String sql = """
            SELECT dns_query AS host,
                count(*) FILTER (WHERE dns_query IS NOT NULL) AS queries,
                count(*) FILTER (WHERE dns_answer IS NOT NULL) AS answers
            FROM net_packets
            WHERE protocol ILIKE 'dns' AND ts_utc > now() - make_interval(secs => ?)
            GROUP BY dns_query
            ORDER BY queries DESC NULLS LAST
        """;
        return jdbc.queryForList(sql, seconds);
    }
}
