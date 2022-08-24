package com.zhang26.weshareserver.service;

import com.zhang26.weshareserver.mapper.GroupAlbumMapper;
import model.GroupAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupAlbumService {
    @Autowired
    GroupAlbumMapper groupAlbumMapper;


    public List<GroupAlbum> getByAlbumId(int albumId){return groupAlbumMapper.getByAlbumId(albumId);}

    public int deleteByAlbumId(int albumId){
        return groupAlbumMapper.deleteByAlbumId(albumId);
    }

    public int insert(GroupAlbum groupAlbum){
        return groupAlbumMapper.insert((groupAlbum));
    }
}
