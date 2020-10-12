package com.imooc.mapper.ustomize;

import com.imooc.pojo.OrderStatus;
import com.imooc.vo.CommentsVO;
import com.imooc.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.jdbc.core.SqlProvider;

import java.util.List;
import java.util.Map;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 18:47
 * </pre>
 */
public interface CommentsCustomizeMapper {

    /**
     * @Description 方法描述
     * @param itemId
     * @param level
     * @author wangyujin
     *  Created on :2020/3/12 10:09
     */
    @SelectProvider(type= CommentsSqlProvider.class, method="selectByCriteria")
    List<CommentsVO> selectByItemIdAndLevel(@Param("itemId") String itemId, @Param("level") Integer level);

    /**
     * @Description: 查询用户评论
     * @author wangyujin
     * @Param: userId:
     * @Param: itemId:
     *  Created on :2020/3/18 22:58
     */
    @SelectProvider(type= CommentsSqlProvider.class, method="selectByUserId")
    List<MyCommentVO> selectByUserId(@Param("userId") String userId,@Param("itemId") String itemId);

    /**
     * @Description: 查询用户评论
     * @author wangyujin
     * @Param: userId:
     * @Param: itemId:
     *  Created on :2020/3/18 22:58
     */
    @SelectProvider(type= CommentsSqlProvider.class, method="selectMyOrderStatusCounts")
    int queryMyOrderStatusCounts(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus, @Param("isComment") Integer isComment);

    /**
     * @Description: 查询用户评论
     * @author wangyujin
     * @Param: userId:
     * @Param: itemId:
     *  Created on :2020/3/18 22:58
     */
    @SelectProvider(type= CommentsSqlProvider.class, method="selectMyOrderStatus")
    List<OrderStatus> queryMyOrderStatus(@Param("userId") String userId);
}
