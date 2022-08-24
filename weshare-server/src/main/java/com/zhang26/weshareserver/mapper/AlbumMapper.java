package com.zhang26.weshareserver.mapper;


import model.Album;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AlbumMapper {
    public Album getById(int albumId);
    public Album getByAlbumName(String albumName);
    public List<Album> getByCreatorId(int creatorId);
    public int deleteById(int albumId);
    public int update(Album album);
    public int insert(Album album);





}
