package fr.pchab.androidrtc;

import android.content.Intent;
import android.os.Bundle;
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
    private String userId;
    private Socket client;
    private String callerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        Bundle extras = getIntent().getExtras();
        callerId = extras.getString("CALLER_ID");
        userId = extras.getString("USER_ID");
        callerName = extras.getString("CALLER_NAME");


        this.mCallerID = (TextView) findViewById(R.id.caller_id);
        this.mCallerID.setText(this.callerName);
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
        finish();
        Intent intent = new Intent(IncomingCallActivity.this, RtcActivity.class);
        intent.putExtra("id", this.userId);
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
