package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 六个最新商品的简单类型
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-04 20:33
 * </pre>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewItemsVO {

    private Integer rootCatId;

    private String rootCatName;

    private String slogan;

    private String catImage;

    private String bgColor;

    private List<SimpleItemVO> simpleItemList;
}
