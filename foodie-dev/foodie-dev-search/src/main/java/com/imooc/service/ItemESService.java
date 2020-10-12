package com.imooc.service;

import com.github.pagehelper.PageInfo;
import com.imooc.es.pojo.Items;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-10-12 13:16
 * </pre>
 */
public interface ItemESService {

    PageInfo<Items> searchItems(String keywords, String sort, Integer pageNum, Integer pageSize);

}
