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
@TableName(value = "items_img")//指定表名
public class ItemsImg {

  @TableId(type= IdType.INPUT)
  private String id;
  private String itemId;
  private String url;
  private long sort;
  private long isMain;
  private Long createdTime;
  private Long updatedTime;

}
