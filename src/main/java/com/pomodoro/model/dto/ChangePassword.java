package com.pomodoro.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePassword {

    @NotBlank(message = "Old password must be filled")
    private String oldPassword;
    @NotBlank(message = "New password must be filled")
    private String newPassword;


}
