package com.pomodoro.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class GroupMessagesRequest {


    @NotBlank(message = "Group name must be filled")
    private String groupName;
    @NotNull(message = "From must be filled")
    private Integer start;
    @NotNull(message = "To must be filled")
    private Integer stop;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStop() {
        return stop;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }
}