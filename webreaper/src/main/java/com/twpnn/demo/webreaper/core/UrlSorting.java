package com.twpnn.demo.webreaper.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.twpnn.demo.webreaper.model.DownloadContent;

public class UrlSorting {

	public static List<DownloadContent> sort(List<DownloadContent> downloadLst){
		if(null == downloadLst) { return downloadLst; }
		if(downloadLst.isEmpty()) { return downloadLst; }
		Collections.sort(downloadLst, new Comparator<DownloadContent>() {
	        @Override public int compare(DownloadContent d1, DownloadContent d2) {
	        	return cp(d1.getTime(), d2.getTime());
	        }
	        <T extends Comparable<T>> int cp(T a, T b) {
	            return a==null ?
	                (b==null ? 0 : Integer.MIN_VALUE) :
	                (b==null ? Integer.MAX_VALUE : a.compareTo(b));
	       }
	    });
		return downloadLst;
	}
}
