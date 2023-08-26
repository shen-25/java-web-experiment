package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.CommentCommand;
import com.jingdong.manager.command.ProductCommand;
import com.jingdong.manager.command.UserCommand;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.exception.BusinessException;
import com.jingdong.manager.exception.BusinessExceptionEnum;
import com.jingdong.manager.model.entity.Comment;
import com.jingdong.manager.model.entity.Product;
import com.jingdong.manager.model.entity.User;
import com.jingdong.manager.service.CommentService;
import com.jingdong.manager.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommentServiceImpl implements CommentService {

    private CommentCommand commentCommand = new CommentCommand();

    private UserCommand userCommand = new UserCommand();
    private ProductCommand productCommand = new ProductCommand();
    @Override
    public void add(Comment comment) {
        User user = userCommand.selectById(comment.getUserId());
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.NEED_USER);
        }
        Product product = productCommand.selectById(comment.getProductId());
        if (product == null) {
            throw new BusinessException(BusinessExceptionEnum.PRODUCT_NOT_EXIST);
        }
        comment.setCreateTime(new Date());
        comment.setReplyStatus(Constant.ReplyStatus.NO_REPLY);
        comment.setStatus(Constant.CommentStatus.NORMAL);
        commentCommand.insert(comment);

    }

    @Override
    public void delete(Long commentId) {
        Comment comment = commentCommand.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(BusinessExceptionEnum.COMMENT_NOT_EXIST);
        }
        commentCommand.deleteById(commentId);

    }

    @Override
    public void reply(Long commentId, String reply, Integer status) {
        Comment comment = commentCommand.selectById(commentId);
        if (comment == null) {
            throw new BusinessException(BusinessExceptionEnum.COMMENT_NOT_EXIST);
        }
        comment.setReply(reply);
        comment.setReplyStatus(Constant.ReplyStatus.REPLIED);
        comment.setReplyTime(new Date());
        comment.setStatus(status);
        commentCommand.edit(comment);

    }

    @Override
    public void editStatus(Long commentId, Integer status) {
        Comment comment = commentCommand.selectById(commentId);
        if (comment != null) {
            throw new BusinessException(BusinessExceptionEnum.COMMENT_NOT_EXIST);
        }
        comment.setReplyStatus(Constant.ReplyStatus.REPLIED);
        comment.setStatus(status);
        commentCommand.edit(comment);
    }

    @Override
    public Map<String, Object> paging(String userName, String productName, String crateTime,Integer status, Integer pageNum, Integer pageSize) {
    	Long NOT_SQL_ID =Long.MIN_VALUE;
        List<Long> userIds = userCommand.userLikeUserNameList(userName);
        List<Long> productIds = productCommand.productLikeProductNameList(productName);
        
        if(userIds == null || userIds.isEmpty()) {
        	userIds = new ArrayList<>();
        	userIds.add(NOT_SQL_ID);
        }
        if(productIds == null || productIds.isEmpty()) {
        	productIds = new ArrayList<>();
        	productIds.add(NOT_SQL_ID);
        }
        Map<String, Object> stringObjectMap = commentCommand.selectPage(userIds, productIds, crateTime, status,pageNum, pageSize);

        return stringObjectMap;
    }

   
}
