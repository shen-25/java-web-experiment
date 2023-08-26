package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.NoticeCommand;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.*;
import com.jingdong.manager.service.NoticeService;

import java.util.Date;
import java.util.Map;

public class NoticeServiceImpl implements NoticeService {

    private NoticeCommand noticeCommand = new NoticeCommand();
    @Override
    public Map<String, Object> paging(String title, String content, Integer pageNum, Integer pageSize) {
        Map<String, Object> stringObjectMap = noticeCommand.selectPage(title, content, pageNum, pageSize);

        return stringObjectMap;
    }

    @Override
    public void add(Notice notice) {
        Notice notice1 = noticeCommand.selectByTitle(notice.getTitle());
        if (notice1 != null) {
            throw new BusinessException(BusinessExceptionEnum.NOTICE_TITLE_EXIST);
        }
        notice.setCreateTime(new Date());
        noticeCommand.insert(notice);
    }

    @Override
    public void edit(Notice notice) {
        Notice notice1 = noticeCommand.selectById(notice.getId());
        if (notice1 == null) {
            throw new BusinessException(BusinessExceptionEnum.NOTICE_NOT_EXIST);
        }
        Notice notice2 = noticeCommand.selectByTitle(notice.getTitle());

        if (notice2 != null && !notice2.getId().equals(notice.getId())) {
            throw new BusinessException(BusinessExceptionEnum.NOTICE_TITLE_EXIST);
        }
        notice1.setContent(notice.getContent());
        notice1.setTitle(notice.getTitle());
        notice1.setUpdateTime(new Date());
        noticeCommand.edit(notice1);
    }

    @Override
    public void delete(Long noticeId) {
        Notice notice = noticeCommand.selectById(noticeId);
        if (notice == null) {
            throw new BusinessException(BusinessExceptionEnum.NOTICE_NOT_EXIST);
        }
        noticeCommand.deleteById(noticeId);
    }
}
