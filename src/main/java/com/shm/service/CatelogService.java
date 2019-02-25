package com.shm.service;

import java.util.List;

import com.shm.pojo.Catelog;

public interface CatelogService {
    public List<Catelog> getAllCatelog();
    public int getCount(Catelog catelog);
    Catelog selectByPrimaryKey(Integer id);
    int updateByPrimaryKey(Catelog record);
    int updateCatelogNum(Integer id,Integer number);
	public List<Catelog> getAllCatelogs();
}
