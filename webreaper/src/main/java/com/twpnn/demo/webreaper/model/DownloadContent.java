package com.twpnn.demo.webreaper.model;

public class DownloadContent implements Comparable<DownloadContent> {
	
	private String name;
    private Double time;
    private Integer status;
    private String errorMsg;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Double getTime() { return time; }
	public void setTime(Double time) { this.time = time; }

	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status;	}
	
	public String getErrorMsg() { return errorMsg; }
	public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
	
	@Override
	public int compareTo(DownloadContent downloadContent) {
		return this.time.compareTo(downloadContent.time);
	}

}
