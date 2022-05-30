package com.example.groupexercise2.model.dto;

import java.util.Date;

public abstract class BaseDto {

  private Date createdAt;
  private Date updatedAt;

  public BaseDto(Date createdAt, Date updatedAt) {
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public BaseDto() {
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

}
