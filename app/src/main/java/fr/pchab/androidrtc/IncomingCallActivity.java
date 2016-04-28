package fr.pchab.androidrtc;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class IncomingCallActivity extends AppCompatActivity {
    private String callerName;
    private TextView mCallerID;
    private String userName;
    private String userId;
    private Socket client;
    private String callerId;
    private Vibrator vib;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        Bundle extras = getIntent().getExtras();
        callerId = extras.getString("CALLER_ID");
        userId = extras.getString("USER_ID");
        callerName = extras.getString("CALLER_NAME");
        userName= extras.getString("USER_NAME");


        this.mCallerID = (TextView) findViewById(R.id.caller_id);
        this.mCallerID.setText(this.callerName);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.skype_call);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        vib= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        vib.vibrate(pattern,0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void acceptCall(View view) {
        vib.cancel();
        mMediaPlayer.stop();
        finish();
        Intent intent = new Intent(IncomingCallActivity.this, RtcActivity.class);
        intent.putExtra("id", this.userId);
        intent.putExtra("name",this.userName);
        intent.putExtra("callerIdChat", callerId);
        //incointent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        String host = "http://" + getResources().getString(R.string.host);
        host += (":" + getResources().getString(R.string.port) + "/");
        try {
            client = IO.socket(host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();
        try {
            JSONObject message = new JSONObject();
            message.put("myId", userId);
            message.put("callerId", callerId);
            client.emit("acceptcall", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Publish a hangup command if rejecting call.
     *
     * @param view
     */
    public void rejectCall(View view) {
        vib.cancel();
        mMediaPlayer.stop();
        finish();
        String host = "http://" + getResources().getString(R.string.host);
        host += (":" + getResources().getString(R.string.port) + "/");
        try {
            client = IO.socket(host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();
        try {
            JSONObject message = new JSONObject();
            message.put("myId", userId);
            message.put("callerId", callerId);
            client.emit("ejectcall", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
