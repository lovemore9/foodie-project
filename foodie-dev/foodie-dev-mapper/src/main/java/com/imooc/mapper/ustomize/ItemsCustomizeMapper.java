package com.imooc.mapper.ustomize;

import com.imooc.vo.SearchItemVO;
import com.imooc.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-12 11:18
 * </pre>
 */
public interface ItemsCustomizeMapper {

    /**
     * @Description: 关键词以及分类进行搜索
     * @author wangyujin
     * @Param: catId:
     * @Param: keywords:
     * @Param: sort:
     *  Created on :2020/3/12 22:07
     */
    @SelectProvider(type= ItemsSqlProvider.class, method="selectByCriteria")
    List<SearchItemVO> searchItem(String catId, String keywords, String sort);

    /**
     * @Description: 拼接购物车相关信息
     * @author wangyujin
     * @Param: specIds:
     *  Created on :2020/3/12 22:11
     */
    @SelectProvider(type= ItemsSqlProvider.class, method="selectBySpecIds")
    List<ShopCartVO> queryShopCartBySpecIds(@Param("specIds")List<String> specIds);

    /**
     * @Description: 乐观锁 修改商品库存
     * @author wangyujin
     * @return: int
     *  Created on :2020/3/15 14:12
     */
    @Update("UPDATE items_spec SET stock = stock - #{pendingCounts} WHERE id = #{specId} AND stock >= #{pendingCounts}")
    int modifyItemSpecStock(@Param("specId") String specId, @Param("pendingCounts") Integer pendingCounts);
}
