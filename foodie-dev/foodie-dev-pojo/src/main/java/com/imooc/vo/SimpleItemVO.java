package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 六个最新商品的简单类型
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-04 22:35
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleItemVO {

    private String itemId;

    private String itemName;

    private String itemUrl;

    private String createdTime;
}
