package com.test;

import com.imooc.Application;
import com.imooc.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.facet.FacetRequest;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-29 13:09
 * </pre>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 不建议使用ElasticsearchTemplate 对索引进行管理（创建索引，更新映射，删除索引）
     * 索引类似数据库中的表结构，我们只会对表中数据进行操作，平时不会使用java代码修改表结构
     */

    @Test
    public void creteIndexStu() {
        Stu stu = new Stu();
        stu.setStuId(1002L);
        stu.setName("laowang");
        stu.setAge(1824 );
        stu.setDescription("I wish I am laowang");
        stu.setMoney(12.5F);
        stu.setSign("aa啊！！！");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }

    @Test
    public void deleteIndexStu() {
        esTemplate.deleteIndex(Stu.class);
    }

 //    -------------------------分割线--------------------------------------------

    @Test
    public void updateStuDoc() {
        IndexRequest indexRequest = new IndexRequest();
        Map<String, Object> sourceMap = new HashMap<>();
//        sourceMap.put("description", "我是更新后的");
//        sourceMap.put("age", 33);
//        sourceMap.put("name", "我是更新后的");
        sourceMap.put("money", 7.2);
        indexRequest.source(sourceMap);
        UpdateQuery upquery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1002")
                .withIndexRequest(indexRequest)
                .build();
        esTemplate.update(upquery);
    }

    @Test
    public void selectStuDoc() {
        GetQuery getQuery = new GetQuery();
//        getQuery.setId("1005");
        Stu stu = esTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }

    @Test
    public void delStuDoc() {
        esTemplate.delete(Stu.class, "1005");
    }

    @Test
    public void searchStuDoc() {
        PageRequest pageRequest = PageRequest.of(1, 1);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                                    .withQuery(QueryBuilders.matchQuery("description", "am"))
                                    .withPageable(pageRequest)
                                    .build();
        AggregatedPage<Stu> stus = esTemplate.queryForPage(searchQuery, Stu.class);
        System.out.println(stus);
    }

    @Test
    public void searchHighLightStuDoc() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        SortBuilder sortBuilderMoney = new FieldSortBuilder("money").order(SortOrder.ASC);
        SortBuilder sortBuilderAge = new FieldSortBuilder("age").order(SortOrder.ASC);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withHighlightFields(new HighlightBuilder.Field("description").preTags("<font color='red'>").postTags("</font>"))
                .withQuery(QueryBuilders.matchQuery("description", "am"))
                .withSort(sortBuilderMoney)
                .withSort(sortBuilderAge)
                .withPageable(pageRequest)
                .build();
        AggregatedPage<Stu> stus = esTemplate.queryForPage(searchQuery, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {

                List<Stu> stuListHighlight = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit hit : hits) {
                    HighlightField highlightField = hit.getHighlightFields().get("description");
                    String str = highlightField.getFragments()[0].toString();

                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    Stu stuHl = new Stu();
                    stuHl.setDescription(str);
                    stuHl.setStuId((Long)sourceAsMap.get("id"));
                    stuHl.setName((String)sourceAsMap.get("name"));
                    stuHl.setAge((Integer) sourceAsMap.get("age"));
                    stuHl.setSign((String) sourceAsMap.get("sign"));
                    stuHl.setMoney(Float.valueOf(sourceAsMap.get("money").toString()));
                    System.out.println(str);
                    stuListHighlight.add(stuHl);
                }
                return new AggregatedPageImpl<>((List < T > )stuListHighlight);
            }
        });
        System.out.println(stus.getTotalPages());
        System.out.println(stus.getContent());
    }
}
