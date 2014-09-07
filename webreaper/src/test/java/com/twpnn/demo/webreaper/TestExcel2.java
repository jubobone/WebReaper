package com.twpnn.demo.webreaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twpnn.demo.webreaper.core.UrlFileDownloader;
import com.twpnn.demo.webreaper.model.DownloadContent;

import net.sf.jxls.transformer.XLSTransformer;

public class TestExcel2 {
	private static String templateFileName = "report/template.xls";
    private static String destFileName = "report/result.xls";
    private static final String homepageUrl = "http://www.ba.no";
	private static final String downloadDirectory = TestExcel2.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "download";
    
	public static void main(String[] args) throws Exception {
		System.out.println("start export");
		List<DownloadContent> downloadContents = new ArrayList<DownloadContent>();
		
		UrlFileDownloader downloadMgr = new UrlFileDownloader(homepageUrl, downloadDirectory);
		
		DownloadContent mainPageDownloadContent = downloadMgr.getMainUrlDownloadContent(homepageUrl);
		downloadContents.add(mainPageDownloadContent);
		
		Map<String, List<DownloadContent>> maps = new HashMap<String, List<DownloadContent>>();
	    maps.put("main", downloadContents);
	    XLSTransformer transformer = new XLSTransformer();
		transformer.transformXLS(templateFileName, maps, destFileName);
		System.out.println("end export");
		
		
		
		
	}
}
