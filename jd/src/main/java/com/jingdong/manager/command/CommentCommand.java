package com.jingdong.manager.command;

import com.mysql.cj.util.StringUtils;
import com.jingdong.manager.common.Constant;
import com.jingdong.manager.model.entity.*;
import com.jingdong.manager.model.vo.CommentVo;
import com.jingdong.manager.utils.C3P0Utils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.apache.commons.dbutils.DbUtils.close;

public class CommentCommand {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private BasicRowProcessor basic = new BasicRowProcessor(new GenerousBeanProcessor());

    public void insert(Comment comment ) {
        String sql = " insert into manager_comment (user_id," +
                "product_id, content, score, status, reply, create_time, reply_status)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.insert(conn, sql, new BeanHandler<>(Comment.class, basic), new Object[]{
                    comment.getUserId(), comment.getProductId(), comment.getContent(),
                    comment.getScore(), comment.getStatus(), comment.getReply(), comment.getCreateTime(),
                    comment.getReplyStatus()
            });
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

            }
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *     String sql = " insert into manager_comment (user_id," +
     *                 "product_id, content, score, status, reply, create_time, update_time)" +
     *                 " values (?, ?, ?, ?, ?, ?, ?, ?)";
     * @param comment
     */
    public void edit(Comment comment) {
        String sql = "update manager_comment set user_id = ?," +
                "product_id =?,`content` =?," +
                " score = ?, " +
                "status = ?, reply=?, reply_status = ?,reply_time= ?, create_time = ?" +
                "  where id = ?";

        conn = C3P0Utils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            conn.setAutoCommit(false);
            queryRunner.execute(conn, sql, new BeanHandler<>(Comment.class, basic), new Object[]{
                    comment.getUserId(), comment.getProductId(), comment.getContent(),
                    comment.getScore(), comment.getStatus(), comment.getReply(),comment.getReplyStatus(),
                    comment.getReplyTime(),comment.getCreateTime(),
                    comment.getId()
            });
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

            }
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Comment selectById(Long commentId) {
        String sql = "select * from manager_comment where id = ?";
        Comment comment = null;
        try {
            QueryRunner qr = new QueryRunner();
            conn = C3P0Utils.getConnection();
            comment = qr.query(conn, sql, new BeanHandler<>(Comment.class, basic), new Object[]{commentId});
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return comment;
    }


    public void deleteById(Long commentId) {
        String sql = "delete from manager_comment where id = ?";
        try {
            conn = C3P0Utils.getConnection();
            QueryRunner queryRunner = new QueryRunner();
            conn.setAutoCommit(false);
            queryRunner.update(conn, sql, new Object[]{commentId});
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {

            }
            e.printStackTrace();
        } finally {
            try {
                DbUtils.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * select * from table_demo where time>unix_timestamp('2010-04-01') and time <= unix_timestamp('2010-05-13'')
     * @param userIdList
     * @param productIdList
     * @param crateTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> selectPage(List<Long> userIdList, List<Long> productIdList, String crateTime, Integer status, Integer pageNum, Integer pageSize){
        String sql2 = "select count(*) from manager_comment where ";
        String sql = "select * from manager_comment where ";

        List<Comment> commentList = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        conn = C3P0Utils.getConnection();
        QueryRunner qr = new QueryRunner();

        String addWhere = "";
        if (!StringUtils.isNullOrEmpty(crateTime)) {
            addWhere += "  create_time >= ? and ";
            objects.add(crateTime );
        }
        
        if (status != null && status != -1) {
            addWhere += " reply_status = ? and ";
            objects.add(status);
        }
        if (userIdList != null && ! userIdList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Long id : userIdList) {
                stringBuilder.append(id + ",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            addWhere += "user_id in (" + stringBuilder.toString() + ")  and";
        }

        if (productIdList != null && !productIdList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Long id : productIdList) {
                stringBuilder.append(id + ",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            addWhere += " product_id in (" + stringBuilder.toString() + ") and";
        }

        sql2 += addWhere + " 1 = 1";
        Long total = 0L;
        try {
            total = (Long) qr.query(conn, sql2, new ScalarHandler<>(), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql += (addWhere +  " 1 = 1  limit ?, ?");
        objects.add((pageNum - 1) * pageSize);
        objects.add(pageSize);
        try {
          commentList = (List<Comment>) qr.query(conn,sql, new BeanListHandler<>(Comment.class, basic), objects.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List<CommentVo> commentVoList = toCommentVoList(commentList);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("records", commentVoList);
        map.put("total", total);
        return map;
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        UserCommand userCommand = new UserCommand();
        ProductCommand productCommand = new ProductCommand();
        for (Comment comment : commentList) {
            CommentVo commentVo = new CommentVo();
            commentVo.setId(comment.getId());
            commentVo.setCreateTime(comment.getCreateTime());
            commentVo.setContent(comment.getContent());
            commentVo.setReply(comment.getReply());
            commentVo.setReplyStatus(comment.getReplyStatus());
            commentVo.setStatus(comment.getStatus());
            commentVo.setScore(comment.getScore());
            commentVo.setReplyTime(comment.getReplyTime());
            User user = userCommand.selectById(comment.getUserId());
            commentVo.setUserName(user.getUserName());
            Product product = productCommand.selectById(comment.getProductId());
            commentVo.setProductName(product.getProductName());
            commentVoList.add(commentVo);
        }
        return commentVoList;
    }


    public static void main(String[] args) {
        CommentCommand commentCommand = new CommentCommand();
//        Comment comment = new Comment();
//        comment.setReplyStatus(1);
//        comment.setContent("测试一下");
//        comment.setCreateTime(new Date());
//        comment.setProductId(1L);
//        comment.setScore(1);
//        comment.setStatus(1);
//        comment.setUserId(1L);
//       commentCommand.insert(comment);
        List<Long> longs = new ArrayList<>();
//        longs.add(1L);
//        longs.add(2L);
//        longs.add(3L);
        Map<String, Object> stringObjectMap = commentCommand.selectPage(null, null, "2022-11-22", -1,1, 10);
        System.out.println(stringObjectMap);


//        Comment comment1 = commentCommand.selectById(1L);
//        comment1.setReply("你好");
//        comment1.setReplyTime(new Date());
//
//        commentCommand.edit(comment1);
//        commentCommand.deleteById(3L);


    }
}
