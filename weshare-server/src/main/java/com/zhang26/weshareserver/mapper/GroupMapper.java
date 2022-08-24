package com.zhang26.weshareserver.mapper;

import model.Album;
import model.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {

    public Group getById(int id);
    public Group getByGroupName(String groupName);
    public List<Group> searchByGroupName(String pattern);
    public List<Group> getByCreatorId(int creatorId);
    public Group getByUuid(String uuid);
    public int deleteById(int id);
    public int update(Group group);
    public int insert(Group group);
    public List<Group> getByUserIdAll(@Param("userId") int userId,@Param("pattern") String patter);
    public List<Group> getByUserIdOnlyMine(@Param("userId") int userId,@Param("pattern") String patter);

    //级联查询,动态sql
    public List<Album> getAlbums(@Param("groupId")int groupId, @Param("userId")int userId, @Param("pattern") String pattern, @Param("all") Boolean all);
}
