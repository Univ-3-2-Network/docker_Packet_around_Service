package com.example.demo.net;

import lombok.RequireArgsConstructor;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequireArgsConstructor
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
        
      }
    }
  }
}