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
@TableName(value = "items")//指定表名
public class Items {

  @TableId(type= IdType.INPUT)
  private String id;
  private String itemName;
  private long catId;
  private long rootCatId;
  private long sellCounts;
  private long onOffStatus;
  private String content;
  private Long createdTime;
  private Long updatedTime;

}
