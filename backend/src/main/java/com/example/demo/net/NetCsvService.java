package com.example.demo.net;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NetCsvService{
  private final NetPacketRepository repo;

  public int ingestWiresharkCsv(MultipartFile file) throws Exception{
    try (Reader reader = new BufferedReader(new InputStreamReader (file.getInputStream(), StandardCharsets.UTF_8))){
      CSVFormat fmt = CSVFormat.DEFAULT.builder()
                      .setHeader()
                      .setSkipHeaderRecord(true)
                      .build();
      Iterable<CSVRecord> records = fmt.parse(reader);
      int count = 0;
      for (CSVRecord r : records){
        String time = get(r, "Time");
        String src = get(r, "Source");
        String dst = get(r, "Destination");
        String proto = get(r, "Protocol");
        String len = get(r, "Length");
        String info = get(r, "Info"); 

        NetPacketEntity e = new NetPacketEntity();
        e.setTimeRel(parseDouble(time));
        e.setSrc(src); e.setDst(dst); e.setProtocol(proto);
        e.setLength(parseInt(len));
        e.setInfo(info);

        // pattern Parsing: TCP flags || HTTP || DNS
        String lower = info == null ? "" : info.toLowerCase(Locale.ROOT);

        // if protocol == tcp / parsing flags
        if("tcp".equalsIgnoreCase(proto)){
          String flags = extractTcpFlags(info);
          e.setTcpFlags(flags);
        }

        // if protocol == http / parsing path, HTTP ver, answerCode, Host, e.t.c.
        if("http".equalsIgnoreCase(proto)){

          if (lower.startsWith("get ")) { e.setHttpMethod("GET"); e.setHttpPath(extractHttpPath(info)); }
          else if(lower.startsWith("post ")) { e.setHttpMethod("POST"); e.setHttpPath(extractHttpPath(info)); }
          else if(lower.startsWith("http/")) {e.setHttpStatus(extractHttpStatus(info)); }
          if (lower.startsWith("host:")) { e.setHttpHost(info.substring(5).trim()); }
        }

        // if protocol == dns / parsing standard query, ns name || query resp, e.t.c.
        if("dns".equalsIgnoreCase(proto)){
          
          if (lower.contains("standard query")){ e.setDnsQuery(extractDnsQuery(info)); }
          if (lower.contains("response")) { e.setDnsAnswer(extractDnsAnswer(info)); }
        }

        repo.save(e);
        count++;
      }
      return count;
    }
  }

  private static String get(CSVRecord r, String key) { return r.isMapped(key) ? r.get(key) : null; }
  private static Integer parseInt(String s) {
    try { return s == null? null: Integer.parseInt(s.trim()); }
    catch(Exception e) {return null;}
  }
  private static Double parseDouble (String s) {
    try { return s == null? null: Double.parseDouble(s.trim()); }
    catch(Exception e) { return null; }
  }

  // into info / " [SYN, ACK] " → "SYN, ACK" / extracted
  private static String extractTcpFlags(String info) {
    if (info == null) { return null; }
    int i = info.indexOf('['), j = info.indexOf(']');
    return (i>=0 && j >i) ? info.substring(i+1, j) : null;
  }

  // into info / "GET /ex/ex1 " -> "/ex/ex1" / extracted
  private static String extractHttpPath(String info){
    if (info == null ) {return null; }
    String[] parts = info.split(" ");
    
    if (parts.length >= 2) { return parts[1]; }
    return null;
  }

  // into info / "HTTP/1.1 200 OK" -> 200 / extracted
  private Integer extractHttpStatus(String info){
    if (info == null ) {return null; }
    String[] parts = info.split(" ");

    if (parts.length >= 2){
      try { return Integer.parseInt(parts[1]); } catch ( Exception ignored) {}
    }
    return null;
  }

  private 
}