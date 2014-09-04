package com.twpnn.demo.webreaper.manager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.twpnn.demo.webreaper.model.DownloadContent;

public class DownloadManager {

	private String mainUrl;
	private String downloadDirectory;
	
	private static final Logger logger = Logger.getLogger(DownloadManager.class);
	
	public DownloadManager (String mainUrl, String downloadDirectory){
		this.mainUrl = mainUrl;
		this.downloadDirectory = downloadDirectory;
	}
	
	/* get document objects from target url*/
	public Document getUrlDocument(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public Connection.Response getConnectionResponse() {
		Connection.Response response = null;
		try {
			response = Jsoup.connect(mainUrl).ignoreContentType(true).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	private Connection.Response getConnectionResponse(String url) {
		Connection.Response response = null;
		try {
			response = Jsoup.connect(url).ignoreContentType(true).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public List<DownloadContent> getDownloadLst(Document document) throws Exception {
		List<DownloadContent> downloadLst = new ArrayList<DownloadContent>();
		
		Elements links = document.select("a[href]");
		Elements media = document.select("[src]");
		Elements imports = document.select("link[href]");

		print("\nMedia: (%d)", media.size());
		Connection.Response response = null;
		DownloadContent downloadContent = null;
		for (Element src : media) {
			response = getConnectionResponse(src.absUrl("src"));
			downloadContent = downloadFile(src.absUrl("src"), "Media");
			
			if(null != downloadContent.getTime()){
				if (src.tagName().equals("img")){
					print(" * %s: <%s> %sx%s (%s) , %.3f%n, %d", src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),trim(src.attr("alt"), 20), downloadContent.getTime(), response.statusCode());
				}
	            else{
	                print(" * %s: <%s> , %.3f%n, %d", src.tagName(), src.attr("abs:src"), downloadContent.getTime(), response.statusCode());
	            }

			} else {
				downloadContent.setTime(-1.00); //fail to download
				print("%s: <%s>", "There is a problem downloading file : ", src.attr("abs:src"));
			}
			downloadContent.setName(src.attr("abs:src"));
        	downloadContent.setStatus(response.statusCode());
			downloadLst.add(downloadContent);
		}

		print("\nImports: (%d)", imports.size());
		for (Element link : imports) {
			response = getConnectionResponse(link.attr("abs:href"));
			downloadContent = downloadFile(link.attr("abs:href"), "Imports");
			
			if(null != downloadContent.getTime()){
				print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"),	link.attr("rel"));
			}else {
				downloadContent.setTime(-1.00); //fail to download
				print("%s: <%s>", "There is a problem downloading file : ", link.attr("abs:href"));
			}
			
			downloadContent.setName(link.attr("abs:href"));
        	downloadContent.setStatus(response.statusCode());
			downloadLst.add(downloadContent);
		}

		print("\nPages: (%d)", links.size());
		for (Element link : links) {
			response = getConnectionResponse(link.attr("abs:href"));
			downloadContent = downloadFile(link.attr("abs:href"), "Pages");
			
			if(null != downloadContent.getTime()){
				print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35)); 
			}else {
				downloadContent.setTime(-1.00); //fail to download
				print("%s: <%s>", "There is a problem downloading file : ", link.attr("abs:href"));
			}
			downloadContent.setName(link.attr("abs:href"));
        	downloadContent.setStatus(response.statusCode());
			downloadLst.add(downloadContent);	
		}
		return downloadLst;
	}
	
	public DownloadContent downloadFile(String src, String category) throws Exception {
		DownloadContent downloadContent = new DownloadContent();
		Long start = System.nanoTime();
		Long end = null;
		Double totalTime = null;

		String name = null;
	    if( src.equals(mainUrl)){
	    	name = "/home.html";
	    }else if(category.equals("Links")){
	    	name = src.substring(src.indexOf("."));
	    	name = name.replace(".", "");
	    	name = name.replace("/", "");
	    	name = "/" + name;
	    }else{
			int indexname = src.lastIndexOf("/");
	        if (indexname == src.length()) {
	            src = src.substring(1, indexname);
	        }
	        name = src.substring(indexname, src.length());
	    }
        logger.info(name);
        
        // check whether directory is existing or not
        File file = new File(downloadDirectory + "/" + category);
        if(!file.exists())
        	file.mkdir();
        
		try {
			 //Open a URL Stream
			URL url = new URL(src);
			file = new File(downloadDirectory + "/" + category + name);
	        FileUtils.copyURLToFile(url, file);
	        
	        end = System.nanoTime();
	        long elapsedTime = end - start;
	        totalTime = (double) elapsedTime / 1000000000.0;
	        
	        downloadContent.setTime(totalTime);
		} catch (IOException e) {
			logger.error(e.getMessage());
			downloadContent.setErrorMsg(e.getMessage());
		} 
		return downloadContent;
    }
	
	private static void print(String msg, Object... args) {
		logger.debug(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

}
