package com.shm.service;

import java.util.List;

import com.shm.pojo.Notice;

public interface NoticeService {

	List<Notice> getNoticeList();
	
	public void insertSelective(Notice notice);

	List<Notice> getPageNoticeList(int pageNum, int pageSize);

	int getNoticeNum();
	
	



}
