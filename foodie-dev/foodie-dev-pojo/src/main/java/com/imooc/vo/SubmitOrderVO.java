package com.imooc.vo;

import com.imooc.bo.ShopCartBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于创建订单的VO对象
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-15 9:08
 * </pre>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitOrderVO {

    private String userId;

    private String itemSpecIds;

    private String addressId;

    private Integer payMethod;

    private String leftMsg;

}
