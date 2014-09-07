package com.twpnn.demo.webreaper.model;

public class DownloadContent {
	
	private String name;
	private ContentType type;
    private Double time;
    private Integer status;
    private String errorMsg;

    public DownloadContent () {}
    
    public DownloadContent (String name, ContentType type, Double time, Integer status, String errorMsg) {
    	this.name = name;
    	this.type = type;
    	this.time = time;
    	this.status = status;
    	this.errorMsg = errorMsg;
    }
    
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public ContentType getType() { return type; }
	public void setType(ContentType type) { this.type = type; }
	
	public Double getTime() { return time; }
	public void setTime(Double time) { this.time = time; }

	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status;	}
	
	public String getErrorMsg() { return errorMsg; }
	public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
}
