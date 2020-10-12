package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 三级分类
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-04 20:36
 * </pre>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryVO {

    private String subId;

    private String subName;

    private String subType;

    private Integer subFatherId;
}
