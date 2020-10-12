package com.imooc.vo;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品详情VO
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 11:20
 * </pre>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoVO {

    private Items item;

    private List<ItemsImg> itemImgList;

    private List<ItemsSpec> itemSpecList;

    private ItemsParam itemParams;
}
