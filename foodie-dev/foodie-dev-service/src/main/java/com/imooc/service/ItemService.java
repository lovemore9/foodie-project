package com.imooc.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mapper.ustomize.CommentsCustomizeMapper;
import com.imooc.pojo.*;
import com.imooc.vo.CommentsLevelCountsVO;
import com.imooc.vo.CommentsVO;
import com.imooc.vo.SearchItemVO;
import com.imooc.vo.ShopCartVO;

import java.util.Arrays;
import java.util.List;

/**
 * 商品相关
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 22:39
 * </pre>
 */

public interface ItemService {

    /**
     * @Description 根据商品主键查询商品信息
     * @param itemId
     * @return com.imooc.pojo.Items
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 10:08
     *  </pre>
     */
    Items queryItemById(String itemId);

    /**
     * @Description 查询商品图片列表
     * @param itemId
     * @return java.util.List<com.imooc.pojo.ItemsImg>
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 10:11
     *  </pre>
     */
    List<ItemsImg> queryItemImgListById(String itemId);

    /**
     * @Description 查询商品规格
     * @param itemId
     * @return java.util.List<com.imooc.pojo.ItemsImg>
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 10:11
     *  </pre>
     */
    List<ItemsSpec> queryItemSpecListById(String itemId);

    /**
     * @Description 查询商品备注
     * @param itemId
     * @return java.util.List<com.imooc.pojo.ItemsSpec>
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 10:23
     *  </pre>
     */
    ItemsParam queryItemParamListById(String itemId);

    /**
     * @Description 根据商品ID查询评论数量
     * @param itemId
     * @return void
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 13:19
     *  </pre>
     */
    CommentsLevelCountsVO queryCommentCount(String itemId);

    /**
     * @Description 根据商品ID查询评论详情
     * @param itemId
     * @return void
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/11 13:19
     *  </pre>
     */
    PageInfo<CommentsVO> queryComments(String itemId, Integer level, Integer pageNum, Integer pageSize);

    /**
     * @Description 根据关键词进行搜索
     * @param keywords
     * @param sort
     * @param pageNum
     * @param pageSize
     * @author wangyujin
     *  Created on :2020/3/12 13:14
     */
    PageInfo<SearchItemVO> searchItemsByKeyWords(String keywords, String sort, Integer pageNum, Integer pageSize);

    /**
     * @Description 根据商品类别进行搜索
     * @param catId
     * @param sort
     * @param pageNum
     * @param pageSize
     * @return com.github.pagehelper.PageInfo<com.imooc.vo.SearchItemVO>
     * @author wangyujin
     * @version 1.0
     *  </pre>
     *  Created on :2020/3/12 14:21
     *  </pre>
     */
    PageInfo<SearchItemVO> searchItemsByCatId(String catId, String sort, Integer pageNum, Integer pageSize);

    /**
     * @Description: 前端传入商品id拼接的字符串，用户渲染购物车相关内容
     * @Param: sepcIdsStr:
     * @return: java.util.List<com.imooc.vo.ShopCartVO>
     *  Created on :2020/3/12 22:19
     */
    List<ShopCartVO> queryItemsBySpecIds(String specIdsStr);

    /**
     * @Description: 根据specId查询商品规格
    * @author wangyujin
     * @Param: specIds:
     *  Created on :2020/3/15 10:12
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * @Description: 根据商品Id获取主图
     * @author wangyujin
     * @Param: itemId:
     *  Created on :2020/3/15 10:24
     */
    String queryItemMainImgById(String itemId);

    /**
     * @Description: 减少库存
     * @author wangyujin
     * @Param: specId:
     * @Param: buyCount:
     *  Created on :2020/3/15 10:46
     */
    int decreaseItemSpecStock(String specId, Integer buyCount);


}
