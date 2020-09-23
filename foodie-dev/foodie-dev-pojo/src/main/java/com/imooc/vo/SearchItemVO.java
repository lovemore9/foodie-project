package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索返回
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-12 11:20
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemVO {

    private String itemName;

    private String itemId;

    private long sellCounts;

    private String imgUrl;

    private double price;

    private String catId;

}
