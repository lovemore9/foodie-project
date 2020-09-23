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
@TableName(value = "items_spec")//指定表名
public class ItemsSpec {

  @TableId(type= IdType.INPUT)
  private String id;
  private String itemId;
  private String name;
  private long stock;
  private double discounts;
  private long priceDiscount;
  private long priceNormal;
  private Long createdTime;
  private Long updatedTime;

}
