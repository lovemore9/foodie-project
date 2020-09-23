package com.imooc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.*;
import com.imooc.mapper.ustomize.CommentsCustomizeMapper;
import com.imooc.mapper.ustomize.ItemsCustomizeMapper;
import com.imooc.pojo.*;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.vo.CommentsLevelCountsVO;
import com.imooc.vo.CommentsVO;
import com.imooc.vo.SearchItemVO;
import com.imooc.vo.ShopCartVO;
import enums.CommentLevel;
import enums.YesOrNo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 10:06
 * </pre>
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemsMapper itemsMapper;

    private final ItemsImgMapper itemsImgMapper;

    private final ItemsSpecMapper itemsSpecMapper;

    private final ItemsParamMapper itemsParamMapper;

    private final ItemsCommentsMapper itemsCommentsMapper;

    private final CommentsCustomizeMapper commentsCustomizeMapper;

    private final ItemsCustomizeMapper itemsCustomizeMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public Items queryItemById(String itemId) {
        Items items = itemsMapper.selectOne(
                new QueryWrapper<Items>()
                        .eq("on_off_status", YesOrNo.YES.type)
                        .eq("id", itemId));
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<ItemsImg> queryItemImgListById(String itemId) {
        List<ItemsImg> itemsImgs = itemsImgMapper.selectList(
                new QueryWrapper<ItemsImg>()
                        .eq("item_id", itemId)
                        .orderByAsc("sort"));
        return itemsImgs;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<ItemsSpec> queryItemSpecListById(String itemId) {
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectList(
                new QueryWrapper<ItemsSpec>()
                        .eq("item_id", itemId));
        return itemsSpecs;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public ItemsParam queryItemParamListById(String itemId) {
        ItemsParam itemsSpec = itemsParamMapper.selectOne(
                new QueryWrapper<ItemsParam>()
                        .eq("item_id", itemId));
        return itemsSpec;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public CommentsLevelCountsVO queryCommentCount(String itemId) {

        List<ItemsComments> itemsComments = itemsCommentsMapper.selectList(
                new QueryWrapper<ItemsComments>()
                        .eq("item_id", itemId)
                        .orderByAsc("created_time"));

        long good_itemsComments = itemsComments.parallelStream().filter(c -> c.getCommentLevel() == (long)CommentLevel.GOOD.type).count();
        long normal_itemsComments = itemsComments.parallelStream().filter(c -> c.getCommentLevel() == (long)CommentLevel.NORMAL.type).count();
        long bad_itemsComments = itemsComments.parallelStream().filter(c -> c.getCommentLevel() == (long)CommentLevel.BAD.type).count();

        CommentsLevelCountsVO build = CommentsLevelCountsVO.builder()
                .totalCounts(itemsComments.size())
                .goodCounts((int) good_itemsComments)
                .normalCounts((int) normal_itemsComments)
                .badCounts((int) bad_itemsComments)
                .build();

        return build;
    }

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
    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public PageInfo<CommentsVO> queryComments(String itemId, Integer level, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<CommentsVO> commentsVOS = commentsCustomizeMapper.selectByItemIdAndLevel(itemId, level);

        PageInfo<CommentsVO> itemsCommentsPageInfo = new PageInfo<>(commentsVOS);

        commentsVOS = itemsCommentsPageInfo.getList();

        commentsVOS.forEach(item -> item.setNickname(DesensitizationUtil.commonDisplay(item.getNickname())));

        return itemsCommentsPageInfo;

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public PageInfo<SearchItemVO> searchItemsByKeyWords(String keywords, String sort, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SearchItemVO> searchItemVOS = itemsCustomizeMapper.searchItem(null, keywords, sort);
        PageInfo<SearchItemVO> searchItemVOPageInfo = new PageInfo<>(searchItemVOS);
        return searchItemVOPageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public PageInfo<SearchItemVO> searchItemsByCatId(String catId, String sort, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SearchItemVO> searchItemVOS = itemsCustomizeMapper.searchItem(catId, null, sort);
        PageInfo<SearchItemVO> searchItemVOPageInfo = new PageInfo<>(searchItemVOS);
        return searchItemVOPageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public List<ShopCartVO> queryItemsBySpecIds(String specIdsStr) {
        String[] split = specIdsStr.split(",");
//        List<String> ids = Arrays.asList(split);
        List<String> ids = new ArrayList<>();
        Collections.addAll(ids, split);
        List<ShopCartVO> shopCartVOS = itemsCustomizeMapper.queryShopCartBySpecIds(ids);
        return shopCartVOS;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public ItemsSpec queryItemSpecById(String specId) {
        ItemsSpec itemsSpec = itemsSpecMapper.selectById(specId);
        return itemsSpec;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS) //查询用supperots
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = itemsImgMapper.selectOne(
                new QueryWrapper<ItemsImg>()
                        .eq("item_id", itemId)
                        .eq("is_main", YesOrNo.YES.type));
        return ObjectUtil.isEmpty(itemsImg) ? "" : itemsImg.getUrl();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) //创建用REQUIRED
    public int decreaseItemSpecStock(String specId, Integer buyCount) {
        // 使用synchronized 关键字 不推荐，集群下无用，性能低下
        // 锁数据库：不推荐，导致数据库性能低下

        // 分布式锁 保证数据的一致性 zookeeper redis这两个中间件都可以做到
        // lockUtil.getLock();加锁
        // 目前我们使用乐观锁的机制进行数据的控制
        int result  = itemsCustomizeMapper.modifyItemSpecStock(specId, buyCount);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因库存不足，商品specId:" + specId);
        }
        return result;

        // lockUtil.unLock;解锁
    }


}
