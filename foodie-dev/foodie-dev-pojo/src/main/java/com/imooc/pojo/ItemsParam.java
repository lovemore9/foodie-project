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
@TableName(value = "items_param")//指定表名
public class ItemsParam {

  @TableId(type= IdType.INPUT)
  private String id;
  private String itemId;
  private String producPlace;
  private String footPeriod;
  private String brand;
  private String factoryName;
  private String factoryAddress;
  private String packagingMethod;
  private String weight;
  private String storageMethod;
  private String eatMethod;
  private Long createdTime;
  private Long updatedTime;

}
