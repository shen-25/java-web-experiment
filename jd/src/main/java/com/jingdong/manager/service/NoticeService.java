package com.jingdong.manager.service;

import com.jingdong.manager.model.entity.Notice;

import java.util.Map;

public interface NoticeService {

    public Map<String, Object> paging(String  title, String content, Integer pageNum, Integer pageSize);

    public void add(Notice notice);

    public void edit(Notice notice);
    public void delete(Long noticeId);
}
