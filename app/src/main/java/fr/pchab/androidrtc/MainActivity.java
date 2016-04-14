package fr.pchab.androidrtc;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ListActivity {
    private SharedPreferences mSharedPreferences;
    private String userName;
    private String userId;
    private ListView mHistoryList;
    private HistoryAdapter mHistoryAdapter;
    private UserAdapter mUserAdapter;
    private AutoCompleteTextView mCallNumET;
    private TextView mUsernameTV;
    public ArrayList<HistoryItem> arrayOfUsers;
    private Handler handler = new Handler();
    //public String json_users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        if (!this.mSharedPreferences.contains("USER_ID")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        this.userId = this.mSharedPreferences.getString("USER_ID", "");
        this.userName = this.mSharedPreferences.getString("USER_NAME", "");
        this.mHistoryList = getListView();
        this.mCallNumET = (AutoCompleteTextView) findViewById(R.id.call_num);
        this.mUsernameTV = (TextView) findViewById(R.id.main_username);

        this.mUsernameTV.setText(this.userName);

        ArrayList<User> adapter = new ArrayList<User>();
        String  json_users = "";
        try{
            try {
                json_users= new RetrieveUserTask().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }


        try{
            JSONArray jsonarr = new JSONArray(json_users);
            for (int i = 0; i < jsonarr.length(); i++) {
                JSONObject jsonobj = jsonarr.getJSONObject(i);

                String id = jsonobj.getString("id");
                String name = jsonobj.getString("name");

                //Log.d("minhminh",id + " "+username);
                if(!id.equals(userId)){
                    User x = new User(id,name);
                    adapter.add(x);
                }
            }
        }catch (Exception e) {
        }


        this.mUserAdapter = new UserAdapter(this, adapter);
        mCallNumET.setThreshold(1);//will start working from first character
        mCallNumET.setAdapter(this.mUserAdapter);//setting the adapter data into the AutoCompleteTextView
        //mCallNumET.setTextColor(Color.RED);



        arrayOfUsers = new ArrayList<HistoryItem>();

        String  json_friend = "";
        try{
            try {
                json_friend= new ListFriendsTask().execute(userId).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        try{
        JSONArray jsonarr = new JSONArray(json_friend);
        for (int i = 0; i < jsonarr.length(); i++) {
            JSONObject jsonobj = jsonarr.getJSONObject(i);
            String id = jsonobj.getString("friend_id");
            String name = "";
            String status = "";
            try{
                try {
                    status = new RetrieveStatusTask().execute(id).get();
                    name= new RetrieveName().execute(id).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
            HistoryItem x = new HistoryItem(id,name, status);
            arrayOfUsers.add(x);
        }
        }catch (Exception e) {
        }




//        HistoryItem a = new HistoryItem("11520232", "online");
//        HistoryItem b = new HistoryItem("11520418", "online");
//        arrayOfUsers.add(a);
//        arrayOfUsers.add(b);

        this.mHistoryAdapter = new HistoryAdapter(this, arrayOfUsers);
        this.mHistoryList.setAdapter(this.mHistoryAdapter);
        startHandler();
    }


//    public String getUserJsonString(){
//        String name = "";
//        try{
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet request = new HttpGet("http://192.168.1.19:3000/users");
//            HttpResponse response = httpclient.execute(request);
//            name = EntityUtils.toString(response.getEntity());
//        }catch(Exception e){
//            Log.e("log_tag", "Error in http connection "+e.toString());
//        }
//        return name;
//    }

    class RetrieveUserTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        @Override
        protected String doInBackground(Void... urls) {
            String name = "";
            try{
                HttpClient httpclient = new DefaultHttpClient();
                String host = "http://" + getResources().getString(R.string.host);
                host += (":" + getResources().getString(R.string.port) + "/");
                HttpGet request = new HttpGet(host+"users/"+userId);
                HttpResponse response = httpclient.execute(request);
                name = EntityUtils.toString(response.getEntity());
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }
            return name;
        }

        protected void onPostExecute(String feed) {
        }
    }

    class RetrieveStatusTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        @Override
        protected String doInBackground(String... urls) {
            String name = "";
            String id = urls[0];
            try{
                HttpClient httpclient = new DefaultHttpClient();
                String host = "http://" + getResources().getString(R.string.host);
                host += (":" + getResources().getString(R.string.port) + "/");
                HttpGet request = new HttpGet(host+"status/"+id);
                HttpResponse response = httpclient.execute(request);
                String json_string = EntityUtils.toString(response.getEntity());
                JSONObject x = new JSONObject(json_string);
                int status = x.getInt("status");
                if (status == 1){
                    name = "Online";
                }else{
                    name = "Offline";
                }

            }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }
            return name;
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AddFriendTask extends AsyncTask<String, Void, Integer> {

        private Exception exception;
        @Override
        protected Integer doInBackground(String... urls) {
            String id= urls[0];
            try {
                HttpClient httpClient = new DefaultHttpClient();
                // replace with your url
                String host = "http://" + getResources().getString(R.string.host);
                host += (":" + getResources().getString(R.string.port) + "/");
                HttpPost httpPost = new HttpPost(host+"addFriend");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("username",userId));
                nameValuePair.add(new BasicNameValuePair("friend_id",id));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    Log.d("minh_res", e.getMessage());
                    e.printStackTrace();
                }
                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    String json_string = EntityUtils.toString(response.getEntity());
                    JSONObject json_data = new JSONObject(json_string);
                    int status= json_data.getInt("status");
                    Log.d("minhstatus",Integer.toString(status) );
                    if (status==1){
                        return 1;
                    }

                } catch (ClientProtocolException e) {
                    Log.d("minh_res", "error");
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                    Log.d("minh_res", e.getMessage());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(String feed) {
        }
    }

    class ListFriendsTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        @Override
        protected String doInBackground(String... urls) {
            String json_string ="";
            String user=urls[0];
            try {
                HttpClient httpClient = new DefaultHttpClient();
                // replace with your url
                String host = "http://" + getResources().getString(R.string.host);
                host += (":" + getResources().getString(R.string.port) + "/");
                HttpPost httpPost = new HttpPost(host+"friends");


                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("username", user));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }
                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    json_string = EntityUtils.toString(response.getEntity());


                } catch (ClientProtocolException e) {
                    // Log exception

                    Log.d("minh_res", "error");
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                    Log.d("minh_res", e.getMessage());

                }
            } catch (Exception e) {
            }
            return json_string;
        }

        protected void onPostExecute(String feed) {
        }
    }

    class RetrieveName extends AsyncTask<String, Void, String> {

        private Exception exception;
        @Override
        protected String doInBackground(String... urls) {
            String json_string ="";
            String user=urls[0];

            try {
                HttpClient httpClient = new DefaultHttpClient();
                // replace with your url
                String host = "http://" + getResources().getString(R.string.host);
                host += (":" + getResources().getString(R.string.port) + "/");
                HttpPost httpPost = new HttpPost(host+"friend_name");


                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("username", user));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }
                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    json_string = EntityUtils.toString(response.getEntity());
                    JSONObject x = new JSONObject(json_string);
                    json_string = x.getString("name");
                    Log.d("minhminhminh",json_string);

                } catch (ClientProtocolException e) {
                    // Log exception

                    Log.d("minh_res", "error");
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                    Log.d("minh_res", e.getMessage());

                }
            } catch (Exception e) {
            }
            return json_string;
        }

        protected void onPostExecute(String feed) {
        }
    }

    /**
     * Take the user to a video screen. USER_NAME is a required field.
     *
     * @param view button that is clicked to trigger toVideo
     */
    public void makeCall(String id) {
        String callNum = id;
        if (callNum.isEmpty() || callNum.equals(this.userId)) {
            showToast("Enter a valid user ID to call.");
            return;
        }
        dispatchCall(callNum);
    }

    public void receiverCall(View view) {
        Intent intent = new Intent(MainActivity.this, RtcActivity.class);
        intent.putExtra("id", this.userId);
        startActivity(intent);
    }

    /**
     * TODO: Debate who calls who. Should one be on standby? Or use State API for busy/available
     * Check that user is online. If they are, dispatch the call by publishing to their standby
     * channel. If the publish was successful, then change activities over to the video chat.
     * The called user will then have the option to accept of decline the call. If they accept,
     * they will be brought to the video chat activity as well, to connect video/audio. If
     * they decline, a hangup will be issued, and the VideoChat adapter's onHangup callback will
     * be invoked.
     *
     * @param callNum Number to publish a call to.
     */
    public void dispatchCall(final String callNum) {
        //Log.d("minhcall", this.username + " " + callNum);
        Intent intent = new Intent(MainActivity.this, RtcActivity.class);
        intent.putExtra("id", this.userId);
        intent.putExtra("number", callNum);
        startActivity(intent);
    }

    /**
     * Handle incoming calls. TODO: Implement an accept/reject functionality.
     * @param userId
     */
//    private void dispatchIncomingCall(String userId){
//        showToast("Call from: " + userId);
//        Log.d("minh", "username " + this.username + " main call user: "+userId);
//        Intent intent = new Intent(MainActivity.this, IncomingCallActivity.class);
//        intent.putExtra(Constants.USER_NAME, username);
//        intent.putExtra(Constants.CALL_USER, userId);
//        startActivity(intent);
//    }

//    private void setUserStatus(String status){
//        try {
//            JSONObject state = new JSONObject();
//            state.put(Constants.JSON_STATUS, status);
//            this.mPubNub.setState(this.stdByChannel, this.username, state, new Callback() {
//                @Override
//                public void successCallback(String channel, Object message) {
//                    Log.d("MA-sUS","State Set: " + message.toString());
//                }
//            });
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//    }

//    private void getUserStatus(String userId){
//        String stdByUser = userId + Constants.STDBY_SUFFIX;
//        this.mPubNub.getState(stdByUser, userId, new Callback() {
//            @Override
//            public void successCallback(String channel, Object message) {
//                Log.d("MA-gUS", "User Status: " + message.toString());
//            }
//        });
//    }

    /**
     * Ensures that toast is run on the UI thread.
     *
     * @param message
     */
    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addfriend(String id,String name){
        this.mCallNumET.dismissDropDown();
        int result = 0;
        try {
            result = new AddFriendTask().execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result!=0){
            HistoryItem x = new HistoryItem(id,name, "online");
            arrayOfUsers.add(x);
            mHistoryAdapter.notifyDataSetChanged();
        }else{
            showToast("error occured");
        }
    }

    private void checkFriendStatus(){
        int checkChange = 0;
        for (HistoryItem user : arrayOfUsers) {
            String status;
            try{
                try {
                    status = new RetrieveStatusTask().execute(user.getUserId()).get();
                    if(status != null && !status.isEmpty() && !status.equals(user.getStatus())){
                        user.setStatus(status);
                        checkChange = 1;
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        if (checkChange!=0){
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    public void startHandler()
    {
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                checkFriendStatus();
                handler.postDelayed(this, 10000);
            }
        }, 10000);
    }

    /**
     * Log out, remove username from SharedPreferences, unsubscribe from PubNub, and send user back
     *   to the LoginActivity
     */
//    public void signOut(){
//        this.mPubNub.unsubscribeAll();
//        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
//        edit.remove(Constants.USER_NAME);
//        edit.apply();
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.putExtra("oldUsername", this.username);
//        startActivity(intent);
//    }
}