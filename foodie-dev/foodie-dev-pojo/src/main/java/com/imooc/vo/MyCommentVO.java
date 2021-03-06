package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCommentVO {

    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String sepcName;
    private String itemImg;

}