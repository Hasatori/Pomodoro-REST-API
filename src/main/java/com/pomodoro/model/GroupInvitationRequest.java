package com.pomodoro.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupInvitationRequest {

    @NotNull(message = "Invited user object is missing")
    private User invitedUser;
    @NotNull(message = "Group object os missing")
    private Group group;

}