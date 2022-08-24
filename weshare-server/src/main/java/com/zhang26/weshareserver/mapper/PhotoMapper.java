package com.zhang26.weshareserver.mapper;

import model.Photo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotoMapper {
    Photo getByPhotoId(int photoId);
    int insert(Photo photo);
    int deleteByPhotoId(int photoId);
    int update(Photo photo);

    public List<Photo> getPhotos(@Param("userId") int userId,@Param("albumId") int albumId, @Param("all") Boolean all);
}
