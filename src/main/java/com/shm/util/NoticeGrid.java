package com.shm.util;

import javax.xml.bind.annotation.XmlRootElement;

import com.shm.pojo.Notice;

import java.util.List;


@XmlRootElement
public class NoticeGrid {
    private int current;//当前页面号
    private int rowCount;//每页行数
    private int total;//总行数
    private List<Notice> rows;
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Notice> getRows() {
		return rows;
	}
	public void setRows(List<Notice> rows) {
		this.rows = rows;
	}

 
}
