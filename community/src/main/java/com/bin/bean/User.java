package com.bin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {
   private Integer id;
   private String username;
   private String password;
   private String salt;
   private String email;
   private Integer type;
   private Integer status;
   private String activationCode;
   private String headerUrl;
   private Date createTime;

   public User() {
      //可以默认赋值
   }
}
