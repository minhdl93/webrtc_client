package fr.pchab.androidrtc;

import android.support.v7.app.AppCompatActivity;

public class IncomingCallActivity extends AppCompatActivity {
//    private String username;
//    private String callUser;
//    private TextView mCallerID;
//    private String number;
//    private String userId;
//    private String userName;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_incoming_call);
//        Bundle extras = getIntent().getExtras();
//        number = extras.getString("NUMBERS");
//        userId = extras.getString("USER_ID");
//        name = extras.getString("NAME");
//
//
//        this.mCallerID = (TextView) findViewById(R.id.caller_id);
//        this.mCallerID.setText(this.callUser);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
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
//    public void acceptCall(View view){
//        Intent intent = new Intent(MainActivity.this, RtcActivity.class);
//        intent.putExtra("id", this.userId);
//        intent.putExtra("number", callNum);
//        startActivity(intent);
//    }
//
//    /**
//     * Publish a hangup command if rejecting call.
//     * @param view
//     */
//    public void rejectCall(View view){
//        JSONObject hangupMsg = PnPeerConnectionClient.generateHangupPacket(this.username);
//        this.mPubNub.publish(this.callUser, hangupMsg, new Callback() {
//            @Override
//            public void successCallback(String channel, Object message) {
//                Intent intent = new Intent(IncomingCallActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}
