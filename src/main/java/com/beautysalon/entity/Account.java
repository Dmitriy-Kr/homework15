package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Account extends BaseEntity{
    private String login;
    private String password;
    private Role role;
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", createTime=" + createTime +
                "} " + super.toString();
    }
}
