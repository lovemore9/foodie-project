package com.imooc.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车前端传入接收对象
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-12 16:35
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCartBO {
    private String itemId;
    private String itemName;
    private String specId;
    private String specName;
    private int buyCounts;
    private String priceDiscount;
    private String priceNormal;
    private String itemImgUrl;
}
