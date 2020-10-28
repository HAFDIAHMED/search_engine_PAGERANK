package com.data;

public class HistoryData {
	private int mPageRank, mAlexaRank;
	private String mURL, mCountry, mCity;
	
	public HistoryData(String URL, int pageRank, int alexaRank) {
		mURL = URL;
		mPageRank = pageRank;
		mAlexaRank = alexaRank;
	}
	
	public HistoryData(String URL, int pageRank, int alexaRank, String country, String city) {
		mURL = URL;
		mPageRank = pageRank;
		mAlexaRank = alexaRank;
		mCountry = country;
		mCity = city;
	}
	
	public int getPageRank() {
		return mPageRank;
	}
	
	public int getAlexaRank() {
		return mAlexaRank;
	}
	
	public String getURL() {
		return mURL;
	}
	
	public String getCountry() {
		return mCountry;
	}
	
	public String getCity() {
		return mCity;
	}
	
	public String toString() {
		return mURL + " | PageRank=" +  mPageRank + " | AlexaRank=" + mAlexaRank + "\n";
	}
}
