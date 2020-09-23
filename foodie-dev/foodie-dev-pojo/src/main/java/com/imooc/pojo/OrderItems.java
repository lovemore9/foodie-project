package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_items")//指定表名
public class OrderItems {

  @TableId(type= IdType.INPUT)
  private String id;
  private String orderId;
  private String itemId;
  private String itemImg;
  private String itemName;
  private String itemSpecId;
  private String itemSpecName;
  private long price;
  private long buyCounts;

}
