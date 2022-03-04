package com.bin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTicket {
   private Integer id;
   private Integer userId;
   private String ticket;
   private Integer status;
   private Date expired;
}
