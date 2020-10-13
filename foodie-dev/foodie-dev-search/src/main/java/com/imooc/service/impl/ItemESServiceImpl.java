package com.imooc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.imooc.es.pojo.Items;
import com.imooc.es.pojo.Stu;
import com.imooc.service.ItemESService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-10-12 13:17
 * </pre>
 */
@Slf4j
@Service
public class ItemESServiceImpl implements ItemESService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public PageInfo<Items> searchItems(String keywords, String sort, Integer pageNum, Integer pageSize) {
        Date now = new Date();
        log.info("ES查询开始：{}", now);
        //分页
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        //排序
//        SortBuilder sortBuilderMoney = new FieldSortBuilder("money").order(SortOrder.ASC);
        SortBuilder sortBuilder = null;

        if (ObjectUtil.equal("c", sort)) {
            sortBuilder = new FieldSortBuilder("sellCounts").order(SortOrder.DESC);
        } else if (ObjectUtil.equal("p", sort)) {
            sortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);
        } else {
            //.unmappedType("keyword") 手动指定排序字段的数据类型
            sortBuilder = new FieldSortBuilder("itemName.keyword").unmappedType("keyword").order(SortOrder.ASC);
        }

        String itemNameFiled = "itemName";

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withHighlightFields(new HighlightBuilder.Field(itemNameFiled).preTags("<font color='red'>").postTags("</font>"))
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                .withSort(sortBuilder)
                .withPageable(pageRequest)
                .build();
        AggregatedPage<Items> items = esTemplate.queryForPage(searchQuery, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {

                List<Items> itemsListHighlight = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    HighlightField highlightField = hit.getHighlightFields().get(itemNameFiled);
                    String itemName = highlightField.getFragments()[0].toString();

                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    Items stuHl = new Items();
                    stuHl.setItemName(itemName);
                    stuHl.setItemId((String)sourceAsMap.get("itemId"));
                    stuHl.setImgUrl((String)sourceAsMap.get("imgUrl"));
                    stuHl.setPrice((Integer) sourceAsMap.get("price"));
                    stuHl.setSellCounts((Integer) sourceAsMap.get("sellCounts"));
                    itemsListHighlight.add(stuHl);
                }
                return new AggregatedPageImpl<>((List< T >)itemsListHighlight, pageable, response.getHits().totalHits);
            }
        });
        PageInfo<Items> itemsPageInfo = new PageInfo<>();
        int totalPages = items.getTotalPages();
        itemsPageInfo.setTotal(items.getTotalElements());
        itemsPageInfo.setPages(totalPages);
        itemsPageInfo.setList(items.getContent());
        log.info("查询结果：{}", items.getContent());
        Date date = new Date();
        log.info("ES查询完毕：{}， 耗时：{}毫秒。", date, date.getTime() - now.getTime());
        return itemsPageInfo;
    }
}
