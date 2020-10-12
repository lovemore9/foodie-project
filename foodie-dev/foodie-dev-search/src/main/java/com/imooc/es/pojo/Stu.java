package com.imooc.es.pojo;

import javassist.compiler.ast.Keyword;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-29 13:15
 * </pre>
 */

@Data
@Document(indexName = "stu", type = "_doc")
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private String description;

    @Field(store = true, type = FieldType.Keyword)
    private String sign;

    @Field(store = true)
    private Float money;
}
