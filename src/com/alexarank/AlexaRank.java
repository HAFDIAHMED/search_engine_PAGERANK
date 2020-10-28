package com.alexarank;

/*
 *  Refer to http://www.mkyong.com/java/how-to-get-alexa-ranking-in-java/
 */
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AlexaRank {
	
	public static int getAlexaRank(String domain) {
		 
		int result = 0;
 
		String url = "http://data.alexa.com/data?cli=10&url=" + domain;
 
		try {
 
			URLConnection conn = new URL(url).openConnection();
			InputStream is = conn.getInputStream();
 
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
 
			Element element = doc.getDocumentElement();
 
			NodeList nodeList = element.getElementsByTagName("POPULARITY");
			if (nodeList.getLength() > 0) {
				Element elementAttribute = (Element) nodeList.item(0);
				String ranking = elementAttribute.getAttribute("TEXT");
				if(!"".equals(ranking)){
					result = Integer.valueOf(ranking);
				}
			}
 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return result;
	}
}
