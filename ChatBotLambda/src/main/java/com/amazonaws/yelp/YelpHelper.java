package com.amazonaws.yelp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;

import com.montealegreluis.yelpv3.apacheclient.ApacheHttpClient;
import com.montealegreluis.yelpv3.businesses.Business;
import com.montealegreluis.yelpv3.businesses.SearchResult;
import com.montealegreluis.yelpv3.client.SearchResponse;
import com.montealegreluis.yelpv3.client.YelpClient;
import com.montealegreluis.yelpv3.client.YelpURIs;
import com.montealegreluis.yelpv3.search.Limit;
import com.montealegreluis.yelpv3.search.SearchCriteria;
import com.montealegreluis.yelpv3.search.SortingMode;

public class YelpHelper {
	private static final String TOKEN = "7U6cSZq-wp4o49EOv_fhBPvyWZCCgef8orBcd9e5Miig6UPKjkH4Bwg-e5i53s8kRra8nv2MEAxN6Tj9fttg0f_1vcK8Awbqc75GXu9lvvJjm-WKQ9dOkC9qJmXnW3Yx";
	private final YelpClient yelpClient;
	
	public YelpHelper(){
		yelpClient = new ApacheHttpClient(HttpClientBuilder.create().build(), new YelpURIs());		
	}
	
	public String search(Map<String, Object> parameters) {
		SearchCriteria criteria = SearchCriteria.byLocation((String)parameters.get("city"));
		criteria.withTerm("restaurants");
		criteria.inCategories((String)parameters.get("cuisine"));

		long targetTime = getRequiredTime((String)parameters.get("date"), (String)parameters.get("time"));
		long currentTime = System.currentTimeMillis()/1000;
		if (targetTime < currentTime) {
			return "Please input correct date and time";
		}
		// Yelp API only supports today's time
		// criteria.openAt(targetTime);
		criteria.openAt(currentTime);

		criteria.sortBy(SortingMode.REVIEW_COUNT);
		criteria.limit(Limit.of(3));
		yelpClient.allBusinessesMatching(criteria, TOKEN);
		SearchResult res = SearchResponse.fromOriginalResponse(yelpClient.responseBody()).searchResult();
		StringBuilder sb = new StringBuilder();
		for(Business b : res.businesses) {
			sb.append(b.name+" "+b.location.displayAddress);
			sb.append("\n");
		}

		if (sb.length() == 0) {
			return "There's no suitable suggestion";
		} else {
			return  sb.toString();
		}
	}
	
	private long getRequiredTime(String date, String time) {
		StringBuilder sb = new StringBuilder();
		sb.append(date);
		sb.append('T');
		sb.append(time);
		sb.append(":00.000-0500");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date dateTime;
		long res = 0;
			try {
				dateTime = dateFormat.parse(sb.toString());
				res = (long)dateTime.getTime()/1000;
			} catch (ParseException e) {
				System.out.println(e);
			}
		return res; 
	}
	
}
