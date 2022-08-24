package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.AlbumMapper;
import model.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    @Autowired
    AlbumMapper albumMapper;
//    @CacheEvict(cacheNames="weshare_album", allEntries=true)
    public int deleteByAlbumId(int albumId){
        return albumMapper.deleteById(albumId);
    }

//    @CacheEvict(cacheNames="weshare_album", allEntries=true)
    public int addAlbum(Album album){
        return albumMapper.insert(album);
    }

//    @Cacheable(cacheNames = "weshare_album",keyGenerator = "cacheKeyGenerator")
    public Album getByAlbumId(int albumId){
        return albumMapper.getById(albumId);
    }

//    @CacheEvict(cacheNames="weshare_album", allEntries=true)
    public int update(Album album){
        return albumMapper.update(album);
    }
}
