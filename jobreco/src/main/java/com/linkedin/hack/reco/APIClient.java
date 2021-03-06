package com.linkedin.hack.reco;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.linkedin.hack.reco.pojo.profile.Profile;
import com.linkedin.hack.reco.pojo.search.SearchResults;

public class APIClient {

	private OAuthService getService() {
		return new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey("igfxoojhisoy").apiSecret("Vc9OURhfaNdMFWLi").build();
	}

	private Token getToken() {
		return new Token("733bb4ed-c932-48ed-9564-8edb0e54b2f4",
				"8b5aa80f-d922-43b6-9006-a82fb0c147da");
	}

	public String getJobs(String country) {
		// http://api.linkedin.com/v1/job-search:(jobs,facets)?facet=location,us:84
		OAuthRequest request = new OAuthRequest(
				Verb.GET,
				"http://api.linkedin.com/v1/job-search:(jobs,facets)?facet=location,us:84?format=json");
		getService().signRequest(getToken(), request);
		Response response = request.send();
		return response.getBody();
	}

	public SearchResults getProfiles(String fname, String lname) {
		String url = String
				.format("https://api.linkedin.com/v1/people-search?first-name=%s&last-name=%s&sort=connections&format=json",
						fname, lname);

		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		getService().signRequest(getToken(), request);
		Response response = request.send();
		SearchResults searchResults = JSONParser.getSearchResults(response.getBody());
		return searchResults;
	}

	public Profile getProfileData(String profileId) {
		OAuthRequest request = new OAuthRequest(
				Verb.GET,
				"http://api.linkedin.com/v1/people/id="
						+ profileId
						+ ":(id,first-name,last-name,industry,positions,site-standard-profile-request,skills,educations)?format=json");
		getService().signRequest(getToken(), request);
		Response response = request.send();
		return JSONParser.getProfile(response.getBody());
	}

	public static void main(String[] args) {
		APIClient lc = new APIClient();

		/*
		 * String jobs = lc.getJobs("us"); System.out.println(jobs);
		 */

		Profile profile = lc.getProfileData("LVoFYo5QHJ");
		System.out.println(profile.getFirstName());
		
		


		/*
		 * String memberDetails = lc.getMemberDetails("LVoFYo5QHJ");
		 * System.out.println(memberDetails);
		 */

	}
}
