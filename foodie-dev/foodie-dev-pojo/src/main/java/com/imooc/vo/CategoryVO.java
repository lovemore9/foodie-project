package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类 显示层使用b
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
public class CategoryVO {

    private Integer id;

    private String name;

    private String type;

    private String fatherId;

    private List<SubCategoryVO> subCatList;
}
