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
@TableName(value = "carousel")//指定表名
public class Carousel {

  @TableId(type= IdType.INPUT)
  private String id;
  private String imageUrl;
  private String backgroundColor;
  private String itemId;
  private String catId;
  private long type;
  private long sort;
  private long isShow;
  private Long createTime;
  private Long updateTime;

}
