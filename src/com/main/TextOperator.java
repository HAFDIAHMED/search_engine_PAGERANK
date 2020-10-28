package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.alexarank.AlexaRank;
import com.common.Utils;

import main.java.google.pagerank.PageRank;

public class TextOperator extends Operator {
	
	public TextOperator(String fileName) {
		super(fileName);
	}
	
	@Override
	public void getPageRankAndAlexaRank() {
		File file =  new File(mFileName);
		if (file.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				try {
					String url = null;
					int pageRank = 0, alexaRank = 0;

					while ((url = reader.readLine()) != null) {
						if (url.matches(Utils.REGEX)) { // check whether URL is valid
							System.out.println(url);
							pageRank = PageRank.get(url); // check page rank
							alexaRank = AlexaRank.getAlexaRank(url);
							System.out.println("PR = " + pageRank + ", AR = " + alexaRank);
						}
						else {
							System.out.println("URL not valid");
						}
						
						System.out.println("--------------------------");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
