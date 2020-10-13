package com.imooc.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-10-13 15:46
 * </pre>
 */
public interface FdfsService {

    String upload(MultipartFile file, String fileExtName) throws Exception;
}
