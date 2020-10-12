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
@TableName(value = "user_address")//指定表名
public class UserAddress {

  @TableId(type= IdType.INPUT)
  private String id;
  private String userId;
  private String receiver;
  private String mobile;
  private String province;
  private String city;
  private String district;
  private String detail;
  private String extand;
  private long isDefault;
  private Date createdTime;
  private Date updatedTime;


}
