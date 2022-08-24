package com.zhang26.weshareserver.controller;

import com.zhang26.weshareserver.config.ReturnStatus;
import model.Photo;
import com.zhang26.weshareserver.service.PhotoService;
import com.zhang26.weshareserver.service.UploadService;
import com.zhang26.weshareserver.util.DateUtil;
import com.zhang26.weshareserver.util.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/photo")
public class PhotoController {

    public static final String DIR = "http://47.113.194.136/";
    @Autowired
    public UploadService uploadService;

    @Autowired
    PhotoService photoService;

    @GetMapping("/add")
    public ReturnJsonUtil<Object> add(int creatorId,String filePath,int albumId){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        Photo photo=new Photo();
        photo.setPhotoStatus(1);
        photo.setFilePath(filePath);
        photo.setDate(DateUtil.utilDateToSQLDate(new Date()));
        photo.setCreatorId(creatorId);
        photo.setAlbumId(albumId);
        try{
            photoService.insert(photo);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
            rj.setStatusMsg("图片添加成功");
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("图片添加失败");
        }
        return rj;
    }

    @GetMapping("/getphotos")
    public ReturnJsonUtil<Object> getPhotos(int userId,boolean all,int albumId){
        ReturnJsonUtil<Object> rj=new ReturnJsonUtil<>();
        Boolean allObj=new Boolean(all);
        List<Photo> photos=null;
        try{
            photos=photoService.getPhotos(userId,albumId,allObj);
            rj.setData(photos);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("查询图片失败");
        }
        return rj;
    }
    @PostMapping("/upload")
    public ReturnJsonUtil<Object> uploadFace(MultipartFile file) throws Exception {
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        // 获取上传文件的名称
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("文件不能为空");
            return rj;
        }
        String[] fileNameArr = originalFilename.split("\\.");
        // 文件后缀
        String suffix = fileNameArr[fileNameArr.length - 1];
        // 判断后缀是否符合
        if (!suffix.equalsIgnoreCase("png")
                && !suffix.equalsIgnoreCase("jpg")
                && !suffix.equalsIgnoreCase("jpeg")) {
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("文件格式错误");
            return rj;
        }
        String filePath = uploadService.uploadFastDfs(file, suffix);
        if (StringUtils.isBlank(filePath)) {
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("文件上传失败");
            return rj;
        }
        rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        rj.setStatusMsg("上传成功");
        rj.setData(DIR+filePath);
        return rj;
    }

    @DeleteMapping
    public ReturnJsonUtil<Object> deleteByGroupId(int photoId){
        ReturnJsonUtil<Object> rj = new ReturnJsonUtil<>();
        try{
            photoService.deleteByPhotoIdChangeStatus(photoId);
            rj.setStatus(ReturnStatus.RESPONSE_SUCCESS);
        }catch (Exception e){
            rj.setStatus(ReturnStatus.COMMON_ERROR);
            rj.setStatusMsg("删除失败");
        }
        return rj;
    }
}
