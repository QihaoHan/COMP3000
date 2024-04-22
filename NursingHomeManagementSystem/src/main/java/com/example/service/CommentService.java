package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;


    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        if (comment.getPid() != null) {  // Indicates that this is a reply
            Comment pComment = this.selectById(comment.getPid());
            if (pComment == null) {
                return;
            }
            if (pComment.getRootId() != null) {
                comment.setRootId(pComment.getRootId());
            } else {
                comment.setRootId(pComment.getId());
            }
        }
        commentMapper.insert(comment);
    }


    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }


    public void deepDelete(Integer pid) {
        commentMapper.deleteById(pid);
        List<Comment> children = commentMapper.selectByPid(pid);
        if (CollUtil.isNotEmpty(children)) {
            for (Comment comment : children) {
                pid = comment.getId();
                this.deepDelete(pid);
            }
        }
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }


    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }


    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }


    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }


    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public List<Comment> selectTree(Integer fid, String module) {
        List<Comment> commentList = commentMapper.selectTree(fid, module);
        List<Comment> rootList = commentList.stream().filter(comment -> comment.getPid() == null).collect(Collectors.toList());
        for (Comment root : rootList) {
            Integer rootId = root.getId();
            List<Comment> children = commentList.stream().filter(comment -> rootId.equals(comment.getRootId())).collect(Collectors.toList());
            root.setChildren(children);
        }
        return rootList;
    }

    public Integer selectCount(Integer fid, String module) {
        return commentMapper.selectCount(fid, module);
    }

}