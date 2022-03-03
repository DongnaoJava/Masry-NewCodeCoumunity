package com.bin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class Comment {
   private Integer id;
   private Integer userId;
   private Integer entityType;
   private Integer entityId;
   private Integer targetId;
   private String content;
   private Integer status;
   private Date createTime;

   public Comment() {
      targetId=0;
      status=0;
   }
}
