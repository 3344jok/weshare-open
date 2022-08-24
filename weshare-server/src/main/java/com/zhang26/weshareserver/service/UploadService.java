package com.zhang26.weshareserver.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    /**
     * 使用FastDFS上传文件
     *
     * @param file        要上传的文件
     * @param fileExtName 文件扩展名
     * @return 文件路径
     * @throws IOException 文件上传异常
     */
    public String uploadFastDfs(MultipartFile file, String fileExtName) throws IOException;

}

