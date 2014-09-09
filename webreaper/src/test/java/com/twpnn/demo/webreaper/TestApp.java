package com.twpnn.demo.webreaper;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.twpnn.demo.webreaper.core.UrlSorting;
import com.twpnn.demo.webreaper.model.DownloadContent;

public class TestApp {

	private static List<DownloadContent> mockDownloadLst;
	
	@Before
	public void preparingObject(){
		mockDownloadLst = new ArrayList<DownloadContent>();
		
		DownloadContent dc1 = new DownloadContent();
		dc1.setName("site A");
		dc1.setTime(1.222222);
		
		DownloadContent dc2 = new DownloadContent();
		dc2.setName("site B");
		dc2.setTime(0.0022222);
		
		DownloadContent dc3 = new DownloadContent();
		dc3.setName("site C");
		dc3.setTime(0.000222);
		
		mockDownloadLst.add(dc1);
		mockDownloadLst.add(dc2);
		mockDownloadLst.add(dc3);
	}
	
	@Test
	public void test() {
		List<DownloadContent> sortDownloadLst = UrlSorting.sort(mockDownloadLst);
		// test size is equal or not
		assertTrue(sortDownloadLst.equals(mockDownloadLst));
		// test sorted order
		 Assert.assertEquals (sortDownloadLst, mockDownloadLst);
	}
	
	@After
	public void emptyValue(){
		mockDownloadLst = null;
	}
}	
