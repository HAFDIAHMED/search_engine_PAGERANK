package com.main;

public abstract class Operator {
	protected String mFileName;
	protected EventListener mEventListener;
	public Operator(String fileName) {
		mFileName = fileName;
	}
	public abstract void getPageRankAndAlexaRank();
	
	public interface EventListener {
		public void log(String log);
	}
	public void registerEventLister(EventListener eventListener) {
		mEventListener = eventListener;
	}
}
