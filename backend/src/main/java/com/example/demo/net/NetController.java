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

    // return latest packet
    @GetMapping("/latest")
    public List<Map<String,Object>> latest(@RequestParam(defaultValue="50") int limit){
        limit = Math.max(1, Math.min(500, limit));
        return jdbc.queryForList("SELECT ts_utc, src, dst, protocol, length, info FROM net_packets DESC LIMIT ?", limit);
    } 

    // Analysis DNS query || simple ver
    @GetMapping("/analysis/dns")
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

    // Analysis HTTP || methods, status, path
    @GetMapping("/analysis/http")
    public List<Map<String, Object>> httpAnalysis(@RequestParam(defaultValue="3600") int seconds){
        String sql = """
            SELECT coalesce(http_method, 'RESP') AS kind,
                   coalesce(http_host, '') AS host,
                   coalesce(http_path, '') AS path,
                   coalesce(http_status,0) As status,
                   count(*) AS cnt
            FROM net_packets
            WHERE protocol ILIKE 'http' AND ts_utc > now() - make_interval(secs => ?)
            GROUP BY kind, host, path, status
            ORDER BY cnd DESC
        """;
        return jdbc.queryForList(sql, seconds);
    } 

    // 
    @GetMapping("/analysis/tcp")
    public List<Map<String,Object>> tcpAnalysis(@RequestParam(defaultValue="3600") int seconds){
        String sql = """
            SELECT
                sum(CASE WHEN tcp_flags ILIKE '%SYN%' AND tcp_flags  NOT ILIKE '%ACK%' THEN 1 ELSE 0 END) AS syn,
                sum(CASE WHEN tcp_flags ILIKE '%SYN%' AND tcp_flags  ILIKE '%ACK%' THEN 1 ELSE 0 END) AS syn_ack,
                sum(CASE WHEN tcp_flags ILIKE '%ACK%' AND tcp_flags  AND coalesce(tcp_flags, '') NOT ILIKE '%SYN%' THEN 1 ELSE 0 END) AS ack_only,
                sum(CASE WHEN tcp_flags ILIKE '%RST%' AND tcp_flags ILIKE '%RST%' THEN 1 ELSE 0 END) AS rst,
                count(*) AS total_tcp
            FROM net_packets
            WHERE protocol ILIKE 'tcp' AND ts_utc > now() - make_interval(secs => ?)
        """;
        return jdbc.queryForList(sql, seconds);
    }
}