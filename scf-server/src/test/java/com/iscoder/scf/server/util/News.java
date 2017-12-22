package com.iscoder.scf.server.util;

import java.util.Date;

import com.iscoder.scf.common.annotation.SCFMember;
import com.iscoder.scf.common.annotation.SCFSerializable;

@SCFSerializable
public class News {

  @SCFMember // 标记该字段为需要序列化字段
  private int newsID;

  @SCFMember
  private String title;

  @SCFMember
  private String content;

  @SCFMember
  private Date addTime;

  public News() {

  }

  public News(int newsID, String title, String content, Date addTime) {
    super();
    this.newsID = newsID;
    this.title = title;
    this.content = content;
    this.addTime = addTime;
  }

  public int getNewsID() {
    return newsID;
  }

  public void setNewsID(int newsID) {
    this.newsID = newsID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getAddTime() {
    return addTime;
  }

  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }
}