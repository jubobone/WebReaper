package com.twpnn.demo.webreaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientTutorial {
  
  private static String url = "http://www.opera.com/download/";

  public static void main(String[] args) throws IOException {
	  HttpClient client = HttpClientBuilder.create().build();
	  HttpGet request = new HttpGet(url);
	  HttpResponse response = client.execute(request);
	  
	  HttpEntity entity = response.getEntity();
      if (entity != null) {
          InputStream is = entity.getContent();
    	  int responseCode = response.getStatusLine().getStatusCode();
    	  
    	  String filename = url.substring(url.indexOf("."));
    	  System.out.println("filename = " + filename);
    	  filename = filename.replace(".", "");
    	  filename = filename.replace("/", "");
    	  System.out.println("new filename = " + filename);
          String filePath = "D:/Work/Study/Java/workspace_luna/webreaper/download/" + filename + ".html";
          FileOutputStream fos = new FileOutputStream(new File(filePath));
          int inByte;
          while((inByte = is.read()) != -1) fos.write(inByte);
          
          System.out.println("response code : " + responseCode);
          
          is.close();
          fos.close();
      }

  }
}

/*

InputStream is = entity.getContent();
String filePath = ...;
FileOutputStream fos = new FileOutputStream(new File(filePath)));
int inByte;
while((inByte = is.read()) != -1) fos.write(inByte);
is.close();
fos.close();
  
  
 */
 