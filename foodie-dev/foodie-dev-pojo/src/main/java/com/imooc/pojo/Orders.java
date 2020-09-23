package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "orders")//指定表名
public class Orders {

  @TableId(type= IdType.INPUT)
  private String id;
  private String userId;
  private String receiverName;
  private String receiverMobile;
  private String receiverAddress;
  private Long totalAmount;
  private Long realPayAmount;
  private Long postAmount;
  private Long payMethod;
  private String leftMsg;
  private String extand;
  private Long isComment;
  private Long isDelete;
  private Date createdTime;
  private Date updatedTime;

}
