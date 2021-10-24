package com.beautysalon.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@Accessors(chain = true)
public class Ordering extends BaseEntity {
    private LocalDateTime orderDateTime;
    private Service service;
    private Employee employee;
    private Client client;
    private StatusEnum status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "Ordering{" +
                "orderDateTime=" + orderDateTime +
                ", service=" + service +
                ", employee=" + employee +
                ", client=" + client +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "} " + super.toString();
    }
}
