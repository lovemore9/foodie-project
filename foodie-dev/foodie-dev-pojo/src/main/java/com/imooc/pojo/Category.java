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
@TableName(value = "category")//指定表名
public class Category {

  @TableId(type= IdType.INPUT)
  private long id;
  private String name;
  private long type;
  private long fatherId;
  private String logo;
  private String slogan;
  private String catImage;
  private String bgColor;

}
