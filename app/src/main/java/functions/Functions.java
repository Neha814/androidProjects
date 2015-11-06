package functions;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Functions {

	JSONParser json = new JSONParser();
	//public static String url = "http://phphosting.osvin.net/TakeATask/WEB_API/";

    public static String url="https://takeataskservices.com/WEB_API/";

	/**
	 * Login
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap login(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "login.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
				localHashMap.put("user_id",
						localJSONObject.getString("user_id"));

				localHashMap.put("first_name",
						localJSONObject.getString("first_name"));
				localHashMap.put("last_name",
						localJSONObject.getString("last_name"));
				localHashMap.put("latitude",
						localJSONObject.getString("latitude"));
				localHashMap.put("longitude",
						localJSONObject.getString("longitude"));
				// localHashMap.put("access_token",localJSONObject.getString("access_token"));

				localHashMap.put("email", localJSONObject.getString("email"));
                localHashMap.put("login_via", localJSONObject.getString("login_via"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Register API
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap register(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "signup.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

				JSONObject result = localJSONObject.getJSONObject("Result");

				// localHashMap.put("access_token",
				// result.getString("access_token"));
				localHashMap.put("email", result.getString("email"));
				localHashMap.put("user_id", result.getString("user_id"));
				localHashMap.put("", result.getString("user_id"));
                localHashMap.put("signup_via", result.getString("signup_via"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * terms and conditions
	 */

	public HashMap termsAndConditions(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url
							+ "listTermsandconditions.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");

				JSONArray result = localJSONObject.getJSONArray("result");

				// localHashMap.put("access_token",
				// result.getString("access_token"));
				localHashMap.put("Message",
						result.getJSONObject(0).getString("description"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message", "Error");
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * getUnreadCountOnMainScreen
	 */

	public HashMap GetUnreadMessageCount(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "countMessages.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");

				// localHashMap.put("access_token",
				// result.getString("access_token"));
				localHashMap.put("TotalMessage",
						localJSONObject.getString("TotalMessage"));
                localHashMap.put("TotalBidCount",
                        localJSONObject.getString("TotalBidCount"));

			} else {
				localHashMap.put("ResponseCode", "false");

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * logout
	 */
	public HashMap logout(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "logout.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");

			} else {
				localHashMap.put("ResponseCode", "false");

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * category Listing
	 */

	public ArrayList<HashMap<String, String>> getCategoryList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listCategories.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("id", Data.getJSONObject(i)
							.getString("id"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("image",
							Data.getJSONObject(i).getString("image"));
					localhashMap.put("icon1",
							Data.getJSONObject(i).getString("icon1"));
					localhashMap.put("icon2",
							Data.getJSONObject(i).getString("icon2"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * forgot password
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap forgetPass(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "forgetPassword.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("MessageWhatHappen",
						localJSONObject.getString("MessageWhatHappen"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("MessageWhatHappen",
						localJSONObject.getString("MessageWhatHappen"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * get profile
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap getProfile(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getUser.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");

				JSONObject result = localJSONObject.getJSONObject("Result");
				JSONObject profile = result.getJSONObject("profile");

				localHashMap.put("fname", profile.getString("fname"));
				localHashMap.put("lname", profile.getString("lname"));
				localHashMap.put("email", profile.getString("email"));
				localHashMap.put("dob", profile.getString("dob"));
				localHashMap.put("address", profile.getString("address"));
				localHashMap.put("city", profile.getString("city"));
				localHashMap.put("state", profile.getString("state"));
				localHashMap.put("country", profile.getString("country"));
				localHashMap.put("zipcode", profile.getString("zipcode"));
				localHashMap.put("phone", profile.getString("phone"));
				localHashMap.put("profile_pic",
						profile.getString("profile_pic"));
				localHashMap.put("ratings", profile.getString("ratings"));
				localHashMap.put("background", profile.getString("background"));
				localHashMap.put("skills", profile.getString("skills"));
				localHashMap.put("occupation", profile.getString("occupation"));
				localHashMap.put("language", profile.getString("language"));
				localHashMap.put("member_from",
						profile.getString("member_from"));

				try {
					JSONArray followers_list = result
							.getJSONArray("followers_list");

					Constants.FollowersList.clear();

					for (int i = 0; i < followers_list.length(); i++) {
						HashMap<String, String> localhashMap = new HashMap<String, String>();
						localhashMap.put("follower_id", followers_list
								.getJSONObject(i).getString("follower_id"));
						localhashMap.put(
								"fname",
								followers_list.getJSONObject(i).getString(
										"fname"));
						localhashMap.put(
								"lname",
								followers_list.getJSONObject(i).getString(
										"lname"));
						localhashMap.put(
								"email",
								followers_list.getJSONObject(i).getString(
										"email"));
						localhashMap.put("reviews", followers_list
								.getJSONObject(i).getString("reviews"));
						localhashMap.put("ratings", followers_list
								.getJSONObject(i).getString("ratings"));
						localhashMap.put("profile_pic", followers_list
								.getJSONObject(i).getString("profile_pic"));

						Constants.FollowersList.add(localhashMap);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				localHashMap.put("ResponseCode", "false");

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * current listing of posted tasks
	 */

	public ArrayList<HashMap<String, String>> GetCurrentPostedTask(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listUserTaskPosted.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("task_id", Data.getJSONObject(i)
							.getString("task_id"));
					localhashMap.put("user_id", Data.getJSONObject(i)
							.getString("user_id"));
					localhashMap.put("fname",
							Data.getJSONObject(i).getString("fname"));
					localhashMap.put("lname",
							Data.getJSONObject(i).getString("lname"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("profile_pic"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("description", Data.getJSONObject(i)
							.getString("description"));

					localhashMap.put("file",
							Data.getJSONObject(i).getString("file"));

					localhashMap.put("address", Data.getJSONObject(i)
							.getString("address"));
					localhashMap.put("city",
							Data.getJSONObject(i).getString("city"));
					localhashMap.put("state",
							Data.getJSONObject(i).getString("state"));
					localhashMap.put("country", Data.getJSONObject(i)
							.getString("country"));
					localhashMap.put("zipcode", Data.getJSONObject(i)
							.getString("zipcode"));
					localhashMap.put("price",
							Data.getJSONObject(i).getString("price"));
					localhashMap.put("due_date", Data.getJSONObject(i)
							.getString("due_date"));
					localhashMap.put("category_name", Data.getJSONObject(i)
							.getString("category_name"));
					localhashMap.put("category_id", Data.getJSONObject(i)
							.getString("category_id"));
					localhashMap.put("subcategory_name", Data.getJSONObject(i)
							.getString("subcategory_name"));
                    localhashMap.put("comments", Data.getJSONObject(i)
                            .getString("comments"));

					localhashMap.put("accepted", Data.getJSONObject(i)
							.getString("accepted"));
                    	localhashMap.put("accepted_by", Data.getJSONObject(i).getString("accepted_by"));
                    localhashMap.put("accepted_fname", Data.getJSONObject(i).getString("accepted_fname"));
                    localhashMap.put("accepted_lname", Data.getJSONObject(i).getString("accepted_lname"));
                    localhashMap.put("accepted_pic", Data.getJSONObject(i).getString("accepted_pic"));

                    JSONArray fileArray = Data.getJSONObject(i).getJSONArray("attachments");
                    Constants.AttachmentList.clear();
                    for(int j=0;j<fileArray.length();j++){
                        Constants.AttachmentList.add(fileArray.getString(j));
                    }
                    Constants.AttachmentList.removeAll(Arrays.asList("", null));
                    String ATTACHMENTS = Constants.AttachmentList.toString().replace("[", "")
                            .replace("]", "").replace(", ", ", ");

                    localhashMap.put("attachments", ATTACHMENTS);

					localArrayList1.add(localhashMap);



				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * current listing of performing tasks
	 */

	public ArrayList<HashMap<String, String>> GetCurrentPerformingTask(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url
							+ "listUserTaskPerformed.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {


					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("task_id", Data.getJSONObject(i).getString("task_id"));
					localhashMap.put("title", Data.getJSONObject(i).getString("title"));
					localhashMap.put("description", Data.getJSONObject(i).getString("description"));
					localhashMap.put("price", Data.getJSONObject(i).getString("price"));
					localhashMap.put("user_id", Data.getJSONObject(i).getString("user_id"));

					localhashMap.put("fname", Data.getJSONObject(i).getString("fname"));
					localhashMap.put("lname", Data.getJSONObject(i).getString("lname"));
					localhashMap.put("profile_pic", Data.getJSONObject(i).getString("profile_pic"));
					localhashMap.put("address", Data.getJSONObject(i).getString("address"));
					localhashMap.put("city", Data.getJSONObject(i).getString("city"));
					localhashMap.put("state", Data.getJSONObject(i).getString("state"));
					localhashMap.put("country", Data.getJSONObject(i).getString("country"));
					localhashMap.put("zipcode", Data.getJSONObject(i).getString("zipcode"));

					localhashMap.put("accepted", Data.getJSONObject(i).getString("accepted"));
					localhashMap.put("due_date", Data.getJSONObject(i).getString("due_date"));
					localhashMap.put("category_id", Data.getJSONObject(i).getString("category_id"));
					localhashMap.put("category_name", Data.getJSONObject(i).getString("category_name"));
					localhashMap.put("subcategory_id", Data.getJSONObject(i).getString("subcategory_id"));
					localhashMap.put("subcategory_name", Data.getJSONObject(i).getString("subcategory_name"));
                    localhashMap.put("comments", Data.getJSONObject(i)
                            .getString("comments"));


                    JSONArray fileArray = Data.getJSONObject(i).getJSONArray("attachments");
                    Constants.AttachmentList.clear();
                    for(int j=0;j<fileArray.length();j++){
                        Constants.AttachmentList.add(fileArray.getString(j));
                    }
                    Constants.AttachmentList.removeAll(Arrays.asList("", null));
                    String ATTACHMENTS = Constants.AttachmentList.toString().replace("[", "")
                            .replace("]", "").replace(", ", ", ");

                    localhashMap.put("attachments", ATTACHMENTS);


					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * Message List
	 */

	public ArrayList<HashMap<String, String>> MessageList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listUsers.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("user_id", Data.getJSONObject(i)
							.getString("user_id"));
					localhashMap.put("fname",
							Data.getJSONObject(i).getString("fname"));
					localhashMap.put("lname",
							Data.getJSONObject(i).getString("lname"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("profile_pic"));
					localhashMap.put("messageCount", Data.getJSONObject(i)
							.getString("messageCount"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * change password
	 */

	public HashMap changePassword(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "changePassword.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * credit card details
	 */
	public HashMap CreditCard(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url
							+ "addUserPaymentAccount.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");

			} else {
				localHashMap.put("ResponseCode", "false");

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Review List
	 */

	public ArrayList<HashMap<String, String>> reviewList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getRatingsReceived.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			String avg_rating = localJSONObject.getString("average_ratings");

			Constants.AVERAGE_RATING_USER = avg_rating;

			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("from_id", Data.getJSONObject(i)
							.getString("from_id"));
					localhashMap.put("fromUser", Data.getJSONObject(i)
							.getString("fromUser"));
					localhashMap.put("email",
							Data.getJSONObject(i).getString("email"));
					localhashMap.put("ratings", Data.getJSONObject(i)
							.getString("ratings"));
					localhashMap.put("review",
							Data.getJSONObject(i).getString("review"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * TakeTask List
	 */

	public ArrayList<HashMap<String, String>> taskLIst(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getTask.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONObject("result")
						.getJSONArray("task_detail");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("id", Data.getJSONObject(i)
							.getString("id"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("description", Data.getJSONObject(i)
							.getString("description"));
					localhashMap.put("address", Data.getJSONObject(i)
							.getString("address"));
					localhashMap.put("city",
							Data.getJSONObject(i).getString("city"));
					localhashMap.put("state",
							Data.getJSONObject(i).getString("state"));
					localhashMap.put("country", Data.getJSONObject(i)
							.getString("country"));
					localhashMap.put("zipcode", Data.getJSONObject(i)
							.getString("zipcode"));
					localhashMap.put("due_date", Data.getJSONObject(i)
							.getString("due_date"));
					localhashMap.put("budget",
							Data.getJSONObject(i).getString("budget"));
					localhashMap.put("file",
							Data.getJSONObject(i).getString("file"));
					localhashMap.put("category_id", Data.getJSONObject(i)
							.getString("category_id"));

					localhashMap.put("category_name", Data.getJSONObject(i)
							.getString("category_name"));
					localhashMap.put("subcategory_name", Data.getJSONObject(i)
							.getString("subcategory_name"));
					localhashMap.put("subcategory_id", Data.getJSONObject(i)
							.getString("subcategory_id"));
					localhashMap.put("comments", Data.getJSONObject(i)
							.getString("comments"));

					localhashMap.put("user_id", Data.getJSONObject(i)
							.getString("user_id"));

					localhashMap.put("fname",
							Data.getJSONObject(i).getString("fname"));
					localhashMap.put("lname",
							Data.getJSONObject(i).getString("lname"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("profile_pic"));


                    JSONArray fileArray = Data.getJSONObject(i).getJSONArray("attachments");
                    Constants.AttachmentList.clear();
                    for(int j=0;j<fileArray.length();j++){
                        Constants.AttachmentList.add(fileArray.getString(j));
                    }
                    Constants.AttachmentList.removeAll(Arrays.asList("", null));
                    String ATTACHMENTS = Constants.AttachmentList.toString().replace("[", "")
                            .replace("]", "").replace(", ", ", ");

                    localhashMap.put("attachments", ATTACHMENTS);

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * Bidding List
	 */

	public ArrayList<HashMap<String, String>> biddingTask(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getTask.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONObject("result")
						.getJSONArray("bidding");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("request_id", Data.getJSONObject(i)
							.getString("request_id"));
					localhashMap.put("price",
							Data.getJSONObject(i).getString("price"));
					localhashMap.put("user_id", Data.getJSONObject(i)
							.getString("user_id"));
					localhashMap.put("fname",
							Data.getJSONObject(i).getString("fname"));
					localhashMap.put("lname",
							Data.getJSONObject(i).getString("lname"));
					localhashMap.put("message", Data.getJSONObject(i)
							.getString("message"));
					localhashMap.put("task_id", Data.getJSONObject(i)
							.getString("task_id"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("profile_pic"));

					/*
					 * localhashMap.put("task_id",
					 * Data.getJSONObject(i).getString("task_id"));
					 * localhashMap.put("title",
					 * Data.getJSONObject(i).getString("title"));
					 * localhashMap.put("description",
					 * Data.getJSONObject(i).getString("description"));
					 * localhashMap.put("price",
					 * Data.getJSONObject(i).getString("price"));
					 * localhashMap.put("user_id",
					 * Data.getJSONObject(i).getString("user_id"));
					 */
					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * API to accept Bid
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap AcceptBid(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "acceptRequest.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap sendNOnce(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "addTransaction.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}


	/**
	 * getPreviousMsgList
	 * 
	 * @param localArrayList
	 * @return
	 */

	public ArrayList<HashMap<String, String>> getPreviousMsgList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "chatting.php?", "POST",
							localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				Constants.TOTAL_PAGE_COUNT = Integer.parseInt(localJSONObject
						.getString("total_count"));

				JSONArray Data = localJSONObject.getJSONArray("Result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("sender_id", Data.getJSONObject(i)
							.getString("sender_id"));
					localhashMap.put("receiver_id", Data.getJSONObject(i)
							.getString("receiver_id"));
					localhashMap.put("message_id", Data.getJSONObject(i)
							.getString("message_id"));
					localhashMap.put("message", Data.getJSONObject(i)
							.getString("message"));
					localhashMap.put("fname",
							Data.getJSONObject(i).getString("fname"));
					localhashMap.put("date",
							Data.getJSONObject(i).getString("date"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("profile_pic"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * API to send message
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap sendMessage(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "chatting.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message_id",
						localJSONObject.getString("Message_id"));

			} else {
				localHashMap.put("ResponseCode", "false");

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Receive Chat
	 */

	public ArrayList<HashMap<String, String>> receiveChat(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "receiveCurrentChat.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");

			try {
				Constants.TOTAL_PAGE_COUNT = Integer.parseInt(localJSONObject
						.getString("total_count"));
			} catch (Exception e) {
				Log.e("total_pg_co exp==>", "" + e);
				e.printStackTrace();
			}
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("sender_id", Data.getJSONObject(i)
							.getString("from_id"));
					localhashMap.put("receiver_id", Data.getJSONObject(i)
							.getString("to_id"));
					localhashMap.put("message_id", Data.getJSONObject(i)
							.getString("message_id"));
					localhashMap.put("message", Data.getJSONObject(i)
							.getString("message"));
					localhashMap.put("fname",
							Data.getJSONObject(i).getString("from_name"));
					localhashMap.put("date",
							Data.getJSONObject(i).getString("date"));
					localhashMap.put("profile_pic", Data.getJSONObject(i)
							.getString("from_profile_pic"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * send Request to accept the task
	 */

	public HashMap SendRequest(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "hireRequest.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap deletePost(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "deleteTask.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap deleteMessage(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "deleteMessage.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap isNotificationEnable(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(
							url + "setNotificationOnOff.php?", "POST",
							localArrayList)).toString());

			String response = localJSONObject.getString("ResponseCode");
			if (response.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("status", localJSONObject.getString("status"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("status", localJSONObject.getString("status"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	public HashMap setNotifications(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(
							url + "setNotificationOnOff.php?", "POST",
							localArrayList)).toString());

			String response = localJSONObject.getString("ResponseCode");
			if (response.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Category Listing
	 */

	public ArrayList<HashMap<String, String>> CategoryListing(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "searchByCategory.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("id", Data.getJSONObject(i)
							.getString("id"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("description", Data.getJSONObject(i)
							.getString("description"));
					localhashMap.put("city",
							Data.getJSONObject(i).getString("city"));
					localhashMap.put("state",
							Data.getJSONObject(i).getString("state"));
					localhashMap.put("country", Data.getJSONObject(i)
							.getString("country"));
					localhashMap.put("address", Data.getJSONObject(i)
							.getString("address"));
					localhashMap.put("due_date", Data.getJSONObject(i)
							.getString("due_date"));
					localhashMap.put("budget",
							Data.getJSONObject(i).getString("budget"));
					localhashMap.put("file",
							Data.getJSONObject(i).getString("file"));
					localhashMap.put("user_id", Data.getJSONObject(i)
							.getString("user_id"));
					localhashMap.put("Name",
							Data.getJSONObject(i).getString("Name"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * Complete task
	 */

	public HashMap completeTask(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "setTaskCompleted.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * close task
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap closeTask(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "setTaskCompleted.php?",
							"POST", localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));

			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Add rating
	 */

	public HashMap giveRate(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "addRating.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("result", localJSONObject.getString("result"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("result", localJSONObject.getString("result"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * FAQ
	 */

	public ArrayList FAQ(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listFAQ.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {

				JSONArray result = localJSONObject.getJSONArray("result");

				for (int i = 0; i < result.length(); i++) {

					HashMap<String, String> localHashMap = new HashMap<String, String>();

					localHashMap.put("id",
							result.getJSONObject(i).getString("id"));
					localHashMap.put("question", result.getJSONObject(i)
							.getString("question"));
					localHashMap.put("ans",
							result.getJSONObject(i).getString("ans"));

					localArrayList1.add(localHashMap);
				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

	/**
	 * incoming payemnts
	 */

	public ArrayList<HashMap<String, String>> IncomingPayments(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listTransactions.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("transaction_id", Data.getJSONObject(i)
							.getString("transaction_id"));
					localhashMap.put("amount", Data.getJSONObject(i)
							.getString("amount"));
					localhashMap.put("name",
							Data.getJSONObject(i).getString("name"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("description", Data.getJSONObject(i)
							.getString("description"));
					localhashMap.put("date",
							Data.getJSONObject(i).getString("date"));




					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

    public ArrayList<HashMap<String, String>> getBiddingNotification(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "listAlertNotifications.php?", "POST",
                            localArrayList)).toString());

            String resopnse = localJSONObject.getString("ResponseCode");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("result");
                for (int i = 0; i < Data.length(); i++) {
                    HashMap<String, String> localhashMap = new HashMap<String, String>();
                    localhashMap.put("id", Data.getJSONObject(i).getString("id"));
                    localhashMap.put("from_id", Data.getJSONObject(i).getString("from_id"));
                    localhashMap.put("from_name", Data.getJSONObject(i).getString("from_name"));
                    localhashMap.put("profile_pic", Data.getJSONObject(i).getString("profile_pic"));
                    localhashMap.put("message", Data.getJSONObject(i).getString("message"));
                    localhashMap.put("title", Data.getJSONObject(i).getString("title"));
                    localhashMap.put("description", Data.getJSONObject(i).getString("description"));
                    localhashMap.put("file", Data.getJSONObject(i).getString("file"));
                    localhashMap.put("task_posted_on", Data.getJSONObject(i).getString("task_posted_on"));



                    localArrayList1.add(localhashMap);

                }

            }
            return localArrayList1;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localArrayList1;

        }

    }
    /**
     * get Pending rating
     */

    public HashMap GetPendingRating(
            ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> localHashMap = new HashMap<String, String>();

        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url
                                    + "listUserForRatings.php?", "POST",
                            localArrayList)).toString());

            String resopnse = localJSONObject.getString("ResponseCode");
            if (resopnse.equalsIgnoreCase("true")) {

                JSONArray Data = localJSONObject.getJSONArray("data");
                //    for (int i = 0; i < Data.length(); i++) {
                //        HashMap<String, String> localhashMap = new HashMap<String, String>();
                localHashMap.put("from_id", Data.getJSONObject(0).getString("from_id"));
                localHashMap.put("to_id", Data.getJSONObject(0).getString("to_id"));
                localHashMap.put("to_name", Data.getJSONObject(0).getString("to_name"));
				localHashMap.put("title", Data.getJSONObject(0).getString("title"));
                localHashMap.put("ResponseCode", "true");


                //   }

            } else {
                localHashMap.put("ResponseCode", "false");
            }

            return localHashMap;
        } catch (Exception ae) {
            ae.printStackTrace();

            return localHashMap;

        }

    }

}
