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
@TableName(value = "users")//指定表名
public class Users {

  @TableId(type= IdType.INPUT)
  private String id;
  private String username;
  private String password;
  private String nickname;
  private String realname;
  private String face;
  private String mobile;
  private String email;
  private long sex;
  private Date birthday;
  private Date createdTime;
  private Date updatedTime;



}
