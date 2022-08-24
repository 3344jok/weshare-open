package com.zhang26.weshareserver.mapper;

import model.GroupAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface GroupAlbumMapper {
    public int insert(GroupAlbum groupAlbum);
    public GroupAlbum getByGroupIdAndAlbumId(@Param("groupId") int groupId, @Param("albumId") int albumId);
    public List<GroupAlbum> getByGroupId(int groupId);
    public List<GroupAlbum> getByAlbumId(int AlbumId);
    public int deleteByAlbumId(int albumId);
}
