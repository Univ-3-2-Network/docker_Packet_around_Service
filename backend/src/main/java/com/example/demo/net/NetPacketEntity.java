package com.example.demo.net;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity @Table(name="net_packets")
public class NetPacketEntity{
  @Id @generatedValue(strategy=GenerationType.IDENTITY)
  private Lond id;

  @Column(name="ts_utc", nullable=false)
  private OffsetDateTime tsUtc = OffsetDateTime.now();

  private Double timeRel;
  private String src;
  private String dst;
  private String protocol;
  private Integer length;
  @Column(length=4000) private String info;

  private String tcpFlags;
  private String httpMethod;
  private String httpHost;
  private String httpPath;
  private Integer httpStatus;

  private String dnsQuery;
  private String dnsAnswer;

  // TODO: need add getter, setter parts
}