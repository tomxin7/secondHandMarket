package com.shm.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.shm.dao.NoticeMapper;
import com.shm.pojo.Notice;
import com.shm.service.NoticeService;

@Service("NoticeService")
public class NoticeServiceImpl implements NoticeService{
	 @Resource
	 private NoticeMapper noticeMapper;

	@Override
	public List<Notice> getNoticeList() {
		
		return noticeMapper.getNoticeList();
	}

	@Override
	public void insertSelective(Notice notice) {
		noticeMapper.insertSelective(notice);
		
	}

	@Override
	public List<Notice> getPageNoticeList(int pageNum, int pageSize) {
		 PageHelper.startPage(pageNum,pageSize);//分页核心代码
		 List<Notice> list= noticeMapper.getNoticeList();
		 return list;
		}

	@Override
	public int getNoticeNum() {
		return 	noticeMapper.getNoticeList().size();
	}
}
