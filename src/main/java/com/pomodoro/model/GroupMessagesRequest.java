package com.pomodoro.model;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class GroupMessagesRequest {


    @NotBlank(message = "Group name must be filled")
    private String groupName;
    @NotBlank(message = "From must be filled")
    private Integer start;
    @NotBlank(message = "To must be filled")
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
