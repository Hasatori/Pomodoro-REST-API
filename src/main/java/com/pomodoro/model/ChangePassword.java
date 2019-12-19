package com.pomodoro.model;

import javax.validation.constraints.NotBlank;

public class ChangePassword {

    @NotBlank(message = "Old password must be filled")
    private String oldPassword;
    @NotBlank(message = "New password must be filled")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
