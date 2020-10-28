package com.geo;

import java.io.File;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;

public class GeoManager {
	public static Geo cityCheck(String url) {
		Geo geo = new Geo();
		
		try {
			File database = new File("db\\GeoLite2-City.mmdb");

			DatabaseReader reader = new DatabaseReader.Builder(database)
					.build();

			CityResponse response = reader.city(InetAddress
					.getByName(url));
			geo.country = response.getCountry().getName();
			geo.city = response.getCity().getName();
			
		} catch (Exception e) {

		}
		
		return geo;
	}
}
