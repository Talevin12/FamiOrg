package com.example.famiorg.logic;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MemberInvitation implements Serializable {
    private String invitationId = String.valueOf(UUID.randomUUID());
    private String famId;
    private String famName;
    private String userSentEmail;
    private String userRecvEmail;
    private Date date;

    public String getInvitationId() {
        return invitationId;
    }

    public MemberInvitation setInvitationId(String invitationId) {
        this.invitationId = invitationId;
        return this;
    }

    public String getFamId() {
        return famId;
    }

    public MemberInvitation setFamId(String famId) {
        this.famId = famId;
        return this;
    }

    public String getFamName() {
        return famName;
    }

    public MemberInvitation setFamName(String famName) {
        this.famName = famName;
        return this;
    }

    public String getUserSentEmail() {
        return userSentEmail;
    }

    public MemberInvitation setUserSentEmail(String userSentEmail) {
        this.userSentEmail = userSentEmail;
        return this;
    }

    public String getUserRecvEmail() {
        return userRecvEmail;
    }

    public MemberInvitation setUserRecvEmail(String userRecvEmail) {
        this.userRecvEmail = userRecvEmail;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public MemberInvitation setDate(Date date) {
        this.date = date;
        return this;
    }
}
