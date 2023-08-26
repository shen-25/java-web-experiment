package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Comment;

import java.util.Map;

public interface CommentService {
    public void add(Comment comment);

    public void delete(Long  commentId);

    public void reply(Long commentId, String reply, Integer status);


    public void editStatus(Long commentId, Integer status);
    public Map<String, Object> paging(String userName, String productName, String crateTime,Integer status,Integer pageNum, Integer pageSize);
}
