package com.imooc.utils;

import java.util.List;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-12 22:15
 * </pre>
 */

public class SqlUtils {
    /**
     * @Description: sql语句在使用 IN 时对集合转为字符串
     * @author wangyujin
     * @return: java.lang.String
     *  Created on :2020/3/12 22:16
     */
    public static String getInSqlStr(List<String> list) {
        StringBuilder str = new StringBuilder();
        str.append("('" + list.get(0)+"'");
        if(list.size()>1){
            for(int i=1;i<list.size();i++){
                str.append(",'"+list.get(i)+"'");
            }
        }
        str.append(")");
        return str.toString();
    }
}
