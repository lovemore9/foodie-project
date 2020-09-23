package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品评价VO
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 13:20
 * </pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsLevelCountsVO {

    private Integer totalCounts;

    private Integer goodCounts;

    private Integer normalCounts;

    private Integer badCounts;
}
