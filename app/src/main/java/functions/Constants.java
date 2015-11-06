package functions;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {

    public static String TERMS_AND_CONDITIONS = "http://takeataskservices.com/terms-conditions.php";
	public static String PRIVACY_POLICY = "http://takeataskservices.com/privacy-policy.php";

	public static String No_INTERNET = "No internet connection.";
	public static String ERROR_MSG = "Something went wrong. Please try again after some time.";

	public static ArrayList<String> GLOBAL_categoryList = new ArrayList<String>();
	public static ArrayList<Integer> GLOBAL_categoryListID = new ArrayList<Integer>();
	public static ArrayList<HashMap<String, String>> FollowersList = new ArrayList<HashMap<String,String>>();

    public static List<String> AttachmentList = new ArrayList<String>();

	public static String EMAIL;
	public static String USER_ID = "";

    public static String LOGIN_TYPE = "";

	public static SharedPreferences mPrefs;

	public static String TYPE;

	public static String AUTH_KEY = "Auth_TakeATask2015";

	public static String TASK_NAME;
	public static String DESCRIBE_TASK;

	public static String ADDRESS;

	public static String COUNTRY;
	public static String CITY;
	public static String STATE;
	public static String ZIPCODE;
	public static String PRICE;
	public static String DATE;
	public static String DATE_TO_SHOW ;
	public static String COMMENTS;
	public static int CAT_POS=0;

	public static String COMMENT_2;

	public static File IMAGE_TO_UPLOAD1 = new File("");

	public static File IMAGE_TO_UPLOAD2 = new File("");

	public static File IMAGE_TO_UPLOAD3 = new File("");

	public static File IMAGE_TO_UPLOAD4 = new File("");

	public static File IMAGE_TO_UPLOAD5 = new File("");

	public static Bitmap TAKENIMAGE;

	public static Bitmap TAKENIMAGE1;
	public static Bitmap TAKENIMAGE2;
	public static Bitmap TAKENIMAGE3;
	public static Bitmap TAKENIMAGE4;
	public static Bitmap TAKENIMAGE5;



public static int ATTACHMENTCOUNT = 0;

	public static double LATITUDE;
	public static double LONGITUDE;

	public static int image_number=0 ;

	public static String CATEGORY_ID;
	public static String CATEGORY_NAME;
	
	public static String GLOBAL_CATEGORY_NAME;

	public static String SENDER_ID;
	public static String RECEIVER_ID;

	public static String CHAT_IMAGE;
	public static String CHAT_NAME;

	public static String AVERAGE_RATING_USER = "0";

	public static String TASK_ID_TO_GET_BIDDING_LIST;

	public static int TOTAL_PAGE_COUNT = 1;

	public static String REVIEW_TASK_ID;

	public static String TASK_DETAIL_TITLE;
	public static String TASK_DETAIL_DESC;
	public static String TASK_DETAIL_PRICE;
	//public static String TASK_DETAIL_URL;
	public static String TASK_DETAIL_DATE;
	public static String TASK_DETAIL_ADDRESS;
	public static String TASK_DETAIL_CITY ;
	public static String TASK_DETAIL_STATE ;
	public static String TASK_DETAIL_COUNTRY ;
	public static String TASK_DETAIL_ZIPCODE ;
	public static String TASK_DETAIL_USERID;
	public static String TASK_DETAIL_ID;
	public static String TASK_DETAIL_FNAME;
	public static String TASK_DETAIL_LNAME ;
    public static String TASK_DETAIL_TASKER_POSTER_NAME ;
    public static String TASK_DETAIL_TASKER_POSTER_ID ;

    public static String TASK_DETAIL_ATTACHMENT_1 ;
    public static String TASK_DETAIL_ATTACHMENT_2 ;
    public static String TASK_DETAIL_ATTACHMENT_3 ;
    public static String TASK_DETAIL_ATTACHMENT_4 ;
    public static String TASK_DETAIL_ATTACHMENT_5 ;

	
	public static String TASK_DETAIL_CATNAME ;
	public static String TASK_DETAIL_SUBCATNAME ;
	public static String TASK_DETAIL_COMMENTS ;
	
	public static String TASK_DETAIL_ACCEPTED ;

	public static String VIEW_PROFILE_ID;

	public static String TASK_DETAIL_DATE_TO_SEND;

	public static String POST4_DATE;

	public static String REGISTRATIO_ID;

	public static String ANSWER1 = "TakeATask is a trusted community marketplace for people and businesses to outsource tasks, find local services or hire flexible staff in minutes- online or via mobile.it's free to post tasks and also a great way to earn money in your spare time."
			+ "\n"
			+ "\n"
			+ "\n"
			+ "Whether its handyman work , cleaning , office admin , photography or something more , you simply post a task for free and then choose from rated , verified and reviewed people ready to work straight away.";

	public static String ANSWER2 = "its easy to signup to TakeATask , just hit the Register Here text to create your free account!"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "You will also see the 'SIGN UP WITH FACEBOOK' option which will automatically create your account using your facebook details so you can get started as fast as possible."
			+ "\n" + "\n" + "Wlcome to TakeATask!";

	public static String ANSWER3 = "TakeATask doesn't charge any money for posting a Task. Once a Task has been completed or passes the task deadline a 15% fee is charged to the worker to keep the lights at TakeATask going. This keeps us ruuning so we can improve the platform even further and intorduce the platform even further and intorduce new features.";

	public static String ANSWER4 = "The TakeATask fee will be charged if you are assigned to a task and is 15% of the task amount (the fee is not charged on any extra expenses incurred during the task.) Once your offer has been accepted for a task you will be liable to pay the fee however to make it easy for the TakeATask we have chose to only charge the fee once the task is complete."
			+ "\n"
			+ "\n"
			+ "\n"
			+ "Once the poster 'releases funds' to the worker we deduct the TakeATask fee before the funds are sent to the workers registered bank account.";

	public static String ANSWER5 = "A Job Poster is a person who wants to find people to help getting everyday tasks and errands done (they 'post' the task). Posting a task is FREE and often only takes a few minutes to find the right person. "
			+ "\n"
			+ "\n"
			+ "\n"
			+ "it's super simple to get started - just create an account and then post your task."
			+ "\n"
			+ "\n"
			+ "Remember to describe your task well and specify where , when and your expected task budget.";

	public static String ANSWER6 = "Absolutely, whether you are worker or a Task Poster, getting things done requires communication."
			+ "\n"
			+ "\n"
			+ "\n"
			+ "That's why we have built a public comment area for each task where you can ask questions about a task and leave answers. Once you assign a worker to a task, you can then communicate through private messages to discuss more details around your task such as locations , private contact details or payment details."
			+ "\n"
			+ "\n"
			+ "Please note , takeATask does not support the exchange of private details in any public areas.";

	public static String QUESTION1 = "What is TakeATask?";

	public static String QUESTION2 = "How do i sign up?";

	public static String QUESTION3 = "How does TakeATask make money ?";

	public static String QUESTION4 = "when is the TakeATask fee charged?";

	public static String QUESTION5 = "What is a Job Poster?";

	public static String QUESTION6 = "Can user contact each other before the task is assigned?";
	
	public static int QUESTION_NUMBER ;
	
	public static String HOME_CATEGORY_ID ;
	
	public static String HOME_CATEGORY_NAME ;
	
	public static String GLOBAL_QUES ;
	public static String GLOBAL_ANS ;


	public static String SEARCH_CAT_ID;
	public static String SEARCH_CAT_NAME;



}
