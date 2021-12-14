package com.webflux.mongo2.project.service.others;

public class ResultProjectTasks {
  private String _id;
  private String name;
  private String taskName;
  private String taskOwnerName;


  public String get_id() {
    return _id;
  }


  public void set_id(String _id) {
    this._id = _id;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getTaskName() {
    return taskName;
  }


  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }


  public String getTaskOwnerName() {
    return taskOwnerName;
  }


  public void setTaskOwnerName(String taskOwnerName) {
    this.taskOwnerName = taskOwnerName;
  }

}