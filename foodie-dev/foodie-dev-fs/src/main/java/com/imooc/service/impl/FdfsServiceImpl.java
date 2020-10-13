package com.imooc.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.service.FdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-10-13 15:47
 * </pre>
 */
@Service
public class FdfsServiceImpl implements FdfsService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String upload(MultipartFile file, String fileExtName) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
        String path = storePath.getFullPath();
        return path;
    }
}
