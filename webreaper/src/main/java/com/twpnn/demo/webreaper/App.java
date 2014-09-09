package com.twpnn.demo.webreaper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.nodes.Document;

import com.twpnn.demo.webreaper.core.UrlFileDownloader;
import com.twpnn.demo.webreaper.core.UrlSorting;
import com.twpnn.demo.webreaper.model.DownloadContent;

public class App {
	
	private static String homepageUrl;
	private static String downloadDirectory;
	private static String templateFileName;
    private static String destFileName;

	private static final Logger logger = Logger.getLogger(App.class);
	
	
	public static void main(String[] args) {

		loadSystemConfiguration();		
		/* 
		 * 1st : download main page and get its document objects which will be use for fetching its links and scripts later
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
		 * 2nd : download main page's links and scripts. 
		 * */
		List<DownloadContent> completeDownloadLst = downloadMgr.getUrlDownloadContents(targetUrlDocument);
		
		completeDownloadLst = UrlSorting.sort(completeDownloadLst);
		
		logger.info("######### Result ( "+ completeDownloadLst.size()+ " )##############");
		
		List<DownloadContent> incompleteDownloadLst = new ArrayList<DownloadContent>();
		
		Iterator<DownloadContent> i = completeDownloadLst.iterator();
		while (i.hasNext()) {
			DownloadContent dw = i.next(); 
			if(null == dw.getTime()){
				incompleteDownloadLst.add(dw);
				i.remove();
			}else {
				logger.info( "<" + dw.getType().toString() + "> : " + dw.getName() + ", with time : " + dw.getTime() + " , with status code : " + dw.getStatus());
			}
			
		}
		
		logger.info("######### Not complete ( "+ incompleteDownloadLst.size()+ " ) ##############");
		for(DownloadContent inDw : incompleteDownloadLst)
			logger.info("<" + inDw.getType().toString() + "> : " + inDw.getName() + " , cannot download; " + inDw.getErrorMsg());
		
		/*
		 * 3rd : create spreadsheet or excel file show the result of download
		 * */
		List<List<DownloadContent>> allDownloadContentLst = new ArrayList<List<DownloadContent>>();
		allDownloadContentLst.add(mainDownloadLst);
		allDownloadContentLst.add(completeDownloadLst);
		allDownloadContentLst.add(incompleteDownloadLst);
		createSpreadSheetReport(allDownloadContentLst);
	}
	
	private static void loadSystemConfiguration(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = App.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(input);
			homepageUrl = prop.getProperty("main_url");
			if (!homepageUrl.startsWith("http://")){
				if(!homepageUrl.startsWith("https://")){
					homepageUrl = "http://" + homepageUrl;
				}
			}
				
			downloadDirectory = prop.getProperty("download_folder");
			templateFileName = prop.getProperty("report_template");
			destFileName = prop.getProperty("report_output");
	 
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private static void createSpreadSheetReport(List<List<DownloadContent>> allDownloadContentLst){
		Map<String, List<DownloadContent>> maps = new HashMap<String, List<DownloadContent>>();
	    maps.put("main", allDownloadContentLst.get(0));
	    maps.put("complete", allDownloadContentLst.get(1));
	    maps.put("incomplete", allDownloadContentLst.get(2));
	    XLSTransformer transformer = new XLSTransformer();
		try {
			transformer.transformXLS(templateFileName, maps, destFileName);
		} catch (ParsePropertyException e) {
			logger.error(e.getMessage());
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}