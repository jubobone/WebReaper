package com.twpnn.demo.webreaper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.twpnn.demo.webreaper.manager.DownloadManager;
import com.twpnn.demo.webreaper.model.DownloadContent;


/**
 * Example program to list links from a URL.
 */
public class ListLinks {
	
	private static final String homepageUrl = "http://www.ba.no";
	private static final String downloadDirectory = "D:/Work/Study/Java/workspace_luna/webreaper/download";

	public static void main(String[] args) throws Exception {

		/*1st step 
		 * fetch all urls and links from the target url via jsoup
		 * in String format
		 * for example, "www.abc.no/ffffdes.js,script"
		 * */
		ListLinks lstLink = new ListLinks();
		
		DownloadManager downloadMgr = new DownloadManager(homepageUrl, downloadDirectory);
		Document targetUrlDocument= downloadMgr.getUrlDocument(homepageUrl);
		
		// download a main page
		System.out.println("download main page time : " + downloadMgr.downloadFile(homepageUrl,"").getTime());
		
		// response status
		Connection.Response response = downloadMgr.getConnectionResponse();
		System.out.println("response status code : " + response.statusCode());
		/*
		 * 2nd step
		 * download each of them and measuring the download time
		 * 
		 * */
		List<DownloadContent> downloadLst = downloadMgr.getDownloadLst(targetUrlDocument);
		Collections.sort(downloadLst);
		
		System.out.println("###############################");
		System.out.println("######### Result ##############");
		
		List<DownloadContent> incompleteDownloadLst = new ArrayList<DownloadContent>();
		
		for(DownloadContent dw : downloadLst){
			if(dw.getTime() < 0) //cannot download with error
				incompleteDownloadLst.add(dw);
			else
				System.out.println("file :" + dw.getName() + ", with time : " + dw.getTime() + " , with status code : " + dw.getStatus());
		}
		System.out.println();
		System.out.println("######### Not complete ##############");
		for(DownloadContent inDw : incompleteDownloadLst)
			System.out.println("file :" + inDw.getName() + " , cannot download; caused by : " + inDw.getErrorMsg());
		
		/*
		 * 3rd step
		 * print the list of download links with the following categories
		 * - download complete
		 * - incomplete
		 * 
		 * */
	}
	
}