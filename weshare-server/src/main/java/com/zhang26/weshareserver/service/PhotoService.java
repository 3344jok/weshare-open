package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.PhotoMapper;
import model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.util.List;

@Service
public class PhotoService {
    @Autowired
    PhotoMapper photoMapper;
    @CacheEvict(cacheNames="weshare_photo", allEntries=true)
    public int insert(Photo photo){
        return photoMapper.insert(photo);
    }

    @Cacheable(cacheNames = "weshare_photo",keyGenerator = "cacheKeyGenerator")
    public List<Photo> getPhotos(int userId, int albumId, Boolean all){
        return photoMapper.getPhotos(userId,albumId,all);
    }

    @CacheEvict(cacheNames="weshare_photo", allEntries=true)
    public int deleteByPhotoIdChangeStatus(int photoId){
        return photoMapper.deleteByPhotoId(photoId);
    }
}
