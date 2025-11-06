package com.example.demo.net;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity @Table(name="net_packets")
public class NetPacketEntity{
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

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
  public Double  setTimeRel() { return timeRel; }
  public String  setSrc() { return src; }
  public String  setDst() { return dst; }
  public String  setProtocol() { return protocol; }
  public Integer setLength() { return length; }
  public String  setTcpFlags() { return tcpFlags; }
  public String  setHttpMethod() { return httpMethod; }
  public String  setHttpHost() { return httpHost; }
  public String  setHttpPath() { return httpPath; }
  public Integer setHttpStatus() { return httpStatus; }
  public String  setDnsQuery() { return dnsQuery; }
  public String  setDnsAnswer() { return dnsAnswer; }

  public Double  getTimeRel() { return timeRel; }
  public String  getSrc() { return src; }
  public String  getDst() { return dst; }
  public String  getProtocol() { return protocol; }
  public Integer getLength() { return length; }
  public String  getTcpFlags() { return tcpFlags; }
  public String  getHttpMethod() { return httpMethod; }
  public String  getHttpHost() { return httpHost; }
  public String  getHttpPath() { return httpPath; }
  public Integer getHttpStatus() { return httpStatus; }
  public String  getDnsQuery() { return dnsQuery; }
  public String  getDnsAnswer() { return dnsAnswer; }
}