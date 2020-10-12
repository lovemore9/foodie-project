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
@TableName(value = "order_status")//指定表名
public class OrderStatus {

  @TableId(type= IdType.INPUT)
  private String orderId;
  private Long orderStatus;
  private Date createdTime;
  private Date payTime;
  private Date deliverTime;
  private Date successTime;
  private Date closeTime;
  private Date commentTime;

}
