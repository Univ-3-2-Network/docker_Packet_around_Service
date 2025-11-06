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

  public void setTimeRel(Double e)     { timeRel = e; }
  public void setSrc(String e)         { src = e; }
  public void setDst(String e)         { dst = e; }
  public void setProtocol(String e)    { protocol = e; }
  public void setLength(Integer e)     { length = e; }
  public void setTcpFlags(String e)    { tcpFlags = e; }
  public void setHttpMethod(String e)  { httpMethod = e; }
  public void setHttpHost(String e)    { httpHost = e; }
  public void setHttpPath(String e)    { httpPath = e; }
  public void setHttpStatus(Integer e) { httpStatus = e; }
  public void setDnsQuery(String e)    { dnsQuery = e; }
  public void setDnsAnswer(String e)   { dnsAnswer = e; }
  public void setInfo(String e)        { info = e;}

  public Double  getTimeRel()          { return timeRel; }
  public String  getSrc()              { return src; }
  public String  getDst()              { return dst; }
  public String  getProtocol()         { return protocol; }
  public Integer getLength()           { return length; }
  public String  getTcpFlags()         { return tcpFlags; }
  public String  getHttpMethod()       { return httpMethod; }
  public String  getHttpHost()         { return httpHost; }
  public String  getHttpPath()         { return httpPath; }
  public Integer getHttpStatus()       { return httpStatus; }
  public String  getDnsQuery()         { return dnsQuery; }
  public String  getDnsAnswer()        { return dnsAnswer; }
  public String  getInfo()             { return info;}
}