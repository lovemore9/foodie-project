package com.imooc.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-12 13:57
 * </pre>
 */

@Controller
public class BaseController {

    public static final String SHOPCART = "shopcart";

    public static final Integer COMMENT_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    //微信支付成功 -> 支付中心 -> 天天吃货平台（回调的url）
    public static final String PAY_RETURN_URL = "http://123.57.69.155:8088/foodie-dev-api/orders/notifyMerchantOrderPaid";

    //慕课网的生产环境下的支付中心地址
    public static final String PAY_MENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //用户头像位置
    public static final String IMAGE_USER_FACE_LOCATION =
            File.separator +
                    "imook-idea-workspace" +
                    File.separator +
                    "images" +
                    File.separator
                    + "foodie" +
                    File.separator
                    + "faces";
}
