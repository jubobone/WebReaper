package com.twpnn.demo.webreaper.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.twpnn.demo.webreaper.model.ContentType;
import com.twpnn.demo.webreaper.model.DownloadContent;

public class UrlFileDownloader {

	private String mainUrl;
	private String downloadDirectory;

	private static final Logger logger = Logger.getLogger(UrlFileDownloader.class);

	public UrlFileDownloader(String mainUrl, String downloadDirectory) {
		this.mainUrl = mainUrl;
		this.downloadDirectory = downloadDirectory;
	}

	/* get document objects from target url */
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

	public DownloadContent getMainUrlDownloadContent(String url) {
		DownloadContent mainUrlDownloadContent = downloadFile(url, ContentType.MAIN);
		return mainUrlDownloadContent;
	}
	
	public List<DownloadContent> getUrlDownloadContents(Document document) {
		List<DownloadContent> downloadLst = new ArrayList<DownloadContent>();

		Elements media = document.select("[src]");
		Elements imports = document.select("link[href]");
		Elements pages = document.select("a[href]");

		logOutputFormat("\nTotal Media: (%d)", media.size());
		DownloadContent downloadContent = null;
		for (Element src : media) {
			downloadContent = downloadFile(src.absUrl("src"), ContentType.MEDIA);
			if(null != downloadContent) {
				if (src.tagName().equals("img")) {
					logOutputFormat(" * %s: <%s> %sx%s (%s) , %.3f%n", src.tagName(),	src.attr("abs:src"), src.attr("width"), src.attr("height"), trim(src.attr("alt"), 20),	downloadContent.getTime());
				} else {
					logOutputFormat(" * %s: <%s> , %.3f%n ", src.tagName(), src.attr("abs:src"), downloadContent.getTime());
				}
			} else {
				logOutputFormat("%s: <%s>", "There is a problem downloading file : ", src.attr("abs:src"));
				downloadContent = new DownloadContent();
				downloadContent.setErrorMsg("Cannot download file from this URL");
			}
			downloadLst.add(downloadContent);
		}

		logOutputFormat("\nTotal Imports: (%d)", imports.size());
		for (Element link : imports) {
			downloadContent = downloadFile(link.attr("abs:href"), ContentType.IMPORT);
			if(null != downloadContent) {
				logOutputFormat(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
			} else {
				logOutputFormat("%s: <%s>", "There is a problem downloading file : ", link.attr("abs:href"));
				downloadContent = new DownloadContent();
				downloadContent.setErrorMsg("Cannot download file from this URL");
			}
			downloadLst.add(downloadContent);
		}

		logOutputFormat("\nTotal Pages: (%d)", pages.size());
		for (Element page : pages) {
			//response = getConnectionResponse(link.attr("abs:href"));
			downloadContent = downloadFile(page.attr("abs:href"), ContentType.PAGE);
			if(null != downloadContent ){
				logOutputFormat(" * a: <%s>  (%s)", page.attr("abs:href"), trim(page.text(), 35));
			} else {
				downloadContent = new DownloadContent();
				downloadContent.setTime(-1.00);
				downloadContent.setErrorMsg("Cannot download file from this URL");
			}
			downloadLst.add(downloadContent);
		}
		return downloadLst;
	}

	private DownloadContent downloadFile(String url, ContentType type) {
		DownloadContent downloadContent = new DownloadContent();
		Long start = System.nanoTime();
		Long end = null;
		Double totalTime = null;

		// check whether directory is existing or not
		File file = new File(prepareDirectory(type));
		if (!file.exists())
			file.mkdirs();

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		InputStream is = null;
		FileOutputStream fos = null;
		int responseCode = 0;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (null != response && null != entity) {
				is = entity.getContent();
				responseCode = response.getStatusLine().getStatusCode();
				String filename = trimFileName(url, type);
				file = new File(filename);
				logger.info("file name : " + file.getName());
				fos = new FileOutputStream(file);
				int inByte;
				while ((inByte = is.read()) != -1)
					fos.write(inByte);

				logger.info("response code : " + responseCode);

				end = System.nanoTime();
				long elapsedTime = end - start;
				totalTime = (double) elapsedTime / 1000000000.0;
				
				downloadContent.setName(url);
				downloadContent.setType(type);
				downloadContent.setStatus(responseCode);
				downloadContent.setTime(totalTime);
				is.close();
				fos.close();
				
			}
		} catch (Exception e){
			logger.error("caused by, " + e.getMessage());
			downloadContent.setName(url);
			downloadContent.setType(type);
			downloadContent.setStatus(responseCode);
			downloadContent.setErrorMsg("caused by, " + e.getMessage());
		}

		return downloadContent;
	}

	private String prepareDirectory(ContentType type){
		if(type == ContentType.MAIN)
			return downloadDirectory + "/";
		else 
			return downloadDirectory + "/" + type;
	}
	
	private String trimFileName(String url, ContentType type){
		String filename = null;
		if (type == ContentType.MAIN) {
			filename = downloadDirectory + "/index.html";
		} else if (type == ContentType.PAGE) {
			filename = url.substring(url.indexOf("."));
			filename = filename.replace(".", "");
			filename = filename.replace("/", "");
			filename = trim(filename, 30);
			filename = downloadDirectory + "/" + type + "/" + filename + ".html";
			logger.info("name of the link's file : " + filename);
		} else { // MEDIA, IMPORT
			int indexname = url.lastIndexOf("/");
			if (indexname == url.length()) {
				url = url.substring(1, indexname);
			}
			filename = downloadDirectory + "/" + type + "/" + url.substring(indexname, url.length());
		}
		return filename;
	}
	
	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1);
		else
			return s;
	}

	private static void logOutputFormat(String msg, Object... args) {
		logger.info(String.format(msg, args));
	}
}
