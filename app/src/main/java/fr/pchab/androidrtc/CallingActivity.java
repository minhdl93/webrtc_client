//package fr.pchab.androidrtc;
//
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.webrtc.MediaStream;
//
//import java.net.URISyntaxException;
//
//import fr.pchab.webrtcclient.WebRtcClient;
//
//public class CallingActivity extends AppCompatActivity implements WebRtcClient.RtcListener{
//    private String callerName;
//    private TextView mCallerID;
//    private String userName;
//    private String userId;
//    private Socket client;
//    private String callerId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_incoming_call);
//
//        Bundle extras = getIntent().getExtras();
//        callerId = extras.getString("CALLER_ID");
//        userId = extras.getString("USER_ID");
//        callerName = extras.getString("CALLER_NAME");
//        userName= extras.getString("USER_NAME");
//
//
//        this.mCallerID = (TextView) findViewById(R.id.caller_id);
//        this.mCallerID.setText(this.callerName);
//        MediaPlayer mMediaPlayer = new MediaPlayer();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.skype_call);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.start();
//        String host = "http://" + getResources().getString(R.string.host);
//        host += (":" + getResources().getString(R.string.port) + "/");
//        try {
//            client = IO.socket(host);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        client.connect();
//        JSONObject message = new JSONObject();
//
//        try {
//            message.put("to", callerId);
//            message.put("type",  "init");
//            message.put("payload", null);
//            client.emit("startclient", message);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * Publish a hangup command if rejecting call.
//     *
//     * @param view
//     */
//    public void rejectCall(View view) {
//        finish();
//        String host = "http://" + getResources().getString(R.string.host);
//        host += (":" + getResources().getString(R.string.port) + "/");
//        try {
//            client = IO.socket(host);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        client.connect();
//        try {
//            JSONObject message = new JSONObject();
//            message.put("myId", userId);
//            message.put("callerId", callerId);
//            client.emit("ejectcall", message);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onCallReady(String callId) {
//
//    }
//
//    @Override
//    public void onAcceptCall(String callId) {
//        try {
//            try{ Thread.sleep(1500); }catch(InterruptedException e){ }
//            answer(callId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * This function is being call to answer call from other user
//     * <p/>
//     * send init message to the caller and connect
//     * start the camera
//     *
//     * @param callerId the id of the caler
//     */
//    public void answer(String callerId) throws JSONException {
//        JSONObject message = new JSONObject();
//        try {
//            message.put("to",callerId );
//            message.put("type", "init");
//            message.put("payload", null);
//            client.emit("message", message);
//            //client.start("android_test");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String newStatus) {
//
//    }
//
//    @Override
//    public void receiveMessage(String id, String msg) {
//
//    }
//
//    @Override
//    public void onLocalStream(MediaStream localStream) {
//
//    }
//
//    @Override
//    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
//
//    }
//
//    @Override
//    public void onRemoveRemoteStream(int endPoint) {
//
//    }
//
//    @Override
//    public void onReceiveCall(String id) {
//
//    }
//}
