package com.raoul.founditt;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class FoundittApplication extends Application {

	static final String TAG = "Panagram";
	
	@Override
	public void onCreate() {
		super.onCreate();		
		
		/*
		 * In this tutorial, we'll subclass ParseObjects for convenience to
		 * create and modify Photo objects.
		 * 
		 * Also, we'll use an Activity class to keep track of the relationships
		 * of ParseUsers with each other and Photos. Every time a user follows, likes 
		 * or comments, a new activity is created to represent the relationship.
		 */
		ParseObject.registerSubclass(Photo.class);
		ParseObject.registerSubclass(Activity.class);
		
		/*
		 * Fill in this section with your Parse credentials
		 */
		Parse.initialize(this, "S9EZezSjlbGTgRv91Uj0EUAPUTmNXgH72Qe3eBXD", "lVJEhLMbdr29s3UoVfR1TqhGKutQ9syuPo1ZAkv3");
		
		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		

		/*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
		ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
		defaultACL.setPublicReadAccess(true);

		/*
		 * Default ACL is public read access, and user read/write access
		 */
		ParseACL.setDefaultACL(defaultACL, true);
		
		/*
		 *  Register for push notifications.
		 */
		PushService.setDefaultPushCallback(this, AlarmActivity.class);

       // PushService.subscribe(this, "Giants", AlarmActivity.class);
        ParsePush.subscribeInBackground("Founditt", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
        ParseUser currentUser=ParseUser.getCurrentUser();

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if(!(currentUser ==null)){

            installation.put("user", ParseUser.getCurrentUser());

        }

        installation.saveInBackground();

//        PushService.subscribeInBackground("", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
//                } else {
//                    Log.e("com.parse.push", "failed to subscribe for push", e);
//                }
//            }
//        });
	}

}
