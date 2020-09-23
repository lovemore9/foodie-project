package com.imooc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-11 18:45
 * </pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsVO {

    private String nickname;

    private String content;

    private String sepcName;

    private Long createdTime;

    private String userFace;
}
