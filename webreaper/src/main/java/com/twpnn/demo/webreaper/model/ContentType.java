package com.twpnn.demo.webreaper.model;

public enum ContentType {
	MAIN, MEDIA, IMPORT, PAGE ;
	
	public String toString() {
        return name().toLowerCase();
    }
}
