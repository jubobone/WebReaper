package com.twpnn.demo.webreaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;

import com.twpnn.demo.webreaper.core.UrlFileDownloader;
import com.twpnn.demo.webreaper.model.DownloadContent;


/**
 * Example program to list links from a URL.
 */
public class App {
	
	private static final String homepageUrl = "http://www.ba.no";
	private static final String downloadDirectory = App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "download";
	private static String templateFileName = "report/template.xls";
    private static String destFileName = "report/result.xls";

	private static final Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) throws Exception {

		/*1st step 
		 * fetch all urls and links from the target url via jsoup
		 * in String format
		 * for example, "www.abc.no/ffffdes.js,script"
		 * */
		UrlFileDownloader downloadMgr = new UrlFileDownloader(homepageUrl, downloadDirectory);
		Document targetUrlDocument= downloadMgr.getUrlDocument(homepageUrl);
		
		DownloadContent mainPageDownloadContent = downloadMgr.getMainUrlDownloadContent(homepageUrl);
		List<DownloadContent> mainDownloadLst = new ArrayList<DownloadContent>();
		mainDownloadLst.add(mainPageDownloadContent);
		// download a main page
		logger.info("download main page time : " + mainPageDownloadContent.getTime());
		
		// response status
		logger.info("response status code : " + mainPageDownloadContent.getStatus());
		
		/*
		 * 2nd step
		 * download each of them and measuring the download time and
		 * print the list of download links with the following categories
		 * - download complete
		 * - incomplete
		 * 
		 * */
		List<DownloadContent> completeDownloadLst = downloadMgr.getUrlDownloadContents(targetUrlDocument);
		
		Collections.sort(completeDownloadLst, new Comparator<DownloadContent>() {
	        @Override public int compare(DownloadContent d1, DownloadContent d2) {
	        	return Double.compare(d1.getTime(), d2.getTime());
	        }

	    });
		
		logger.info("######### Result ( "+ completeDownloadLst.size()+ " )##############");
		
		List<DownloadContent> incompleteDownloadLst = new ArrayList<DownloadContent>();
		
		Iterator<DownloadContent> i = completeDownloadLst.iterator();
		while (i.hasNext()) {
			DownloadContent dw = i.next(); // must be called before you can call i.remove()
			if(dw.getTime() < 0){
				incompleteDownloadLst.add(dw);
				i.remove();
			}else {
				logger.info( "<" + dw.getType().toString() + "> : " + dw.getName() + ", with time : " + dw.getTime() + " , with status code : " + dw.getStatus());
			}
			
		}
		
		logger.info("######### Not complete ( "+ incompleteDownloadLst.size()+ " ) ##############");
		for(DownloadContent inDw : incompleteDownloadLst)
			System.out.println("<" + inDw.getType().toString() + "> : " + inDw.getName() + " , cannot download; " + inDw.getErrorMsg());
		
		/*
		 * step 3 
		 * create spreadsheet or excel file show the result of file download
		 * 
		 * */
		Map<String, List<DownloadContent>> maps = new HashMap<String, List<DownloadContent>>();
	    maps.put("main", mainDownloadLst);
	    maps.put("complete", completeDownloadLst);
	    maps.put("incomplete", incompleteDownloadLst);
	    XLSTransformer transformer = new XLSTransformer();
		transformer.transformXLS(templateFileName, maps, destFileName);
		
	}
}