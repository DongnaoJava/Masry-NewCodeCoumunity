package com.bin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTicket {
    Integer id;
    Integer userId;
    String ticket;
    Integer status;
    Date expired;
}
