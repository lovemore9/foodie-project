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
@TableName(value = "items_comments")//指定表名
public class ItemsComments {

  @TableId(type= IdType.INPUT)
  private String id;
  private String userId;
  private String itemId;
  private String itemName;
  private String itemSpecId;
  private Long commentLevel;
  private String sepcName;
  private String content;
  private Date createdTime;
  private Date updatedTime;

}
