package com.zhang26.weshareserver.mapper;

import model.WebMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebMessageMapper {
    public int insert(WebMessage webMessage);
    public int update(int id);
    public int updateAll(String userName);
    public List<WebMessage> getReadByUserName(String userName);
    public List<WebMessage> getUnReadByUserName(String userName);
    public int getUnReadNumByUserName(String userName);
}
