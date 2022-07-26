package coza.rubiblue.dashpay;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NativePlugin(
        requestCodes={DashPayModule.PRINT_REQUEST_CODE}
)
public class DashPayModule extends Plugin {
    protected static final int PRINT_REQUEST_CODE = 2; // Unique request code
    private static final String PAYMENT_URI = "com.ar.dashpaypos";
    private static final int REQUEST_CODE = 1;
    public static int tsn=1;
    public static String lastSentTsn="";
    private PluginCall mReturnResults;

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod
    public void getSerial(PluginCall call) {
        try {
            JSObject ret = new JSObject();
            String SerialNumber = Build.SERIAL;
            if (SerialNumber == "unknown") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SerialNumber = Build.getSerial();
                }
            }
            ret.put("value", SerialNumber);
            call.success(ret);
        } catch (Exception ex) {
            JSObject ret = new JSObject();
            ret.put("value", "unknown");
            call.success(ret);
        }
    }

    @PluginMethod()
    public void print(PluginCall call) {
        try {
            Context context = this.getBridge().getActivity().getApplicationContext();
            String printString = call.getString("printString");
            String EXTRA_ORIGINATING_URI = call.getString("EXTRA_ORIGINATING_URI");
            Boolean NewActivityLaunchOption = call.getBoolean("NewActivityLaunchOption", false);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains("com.dashpay.bridge") ||
                        info.activityInfo.name.toLowerCase().contains("com.dashpay.bridge")) {
                    share.putExtra(Intent.EXTRA_ORIGINATING_URI, EXTRA_ORIGINATING_URI);
                    share.putExtra("key", "Print");
                    share.putExtra("printString", printString);
                    share.setPackage(info.activityInfo.packageName);
                    JSObject ret = new JSObject();
                    if (NewActivityLaunchOption == false) {
                        startActivityForResult(call, Intent.createChooser(share, "Select"), PRINT_REQUEST_CODE);
                        ret.put("value", "printing");
                    } else {
                        this.getBridge().getActivity().startActivityForResult(Intent.createChooser(share, "Select"), PRINT_REQUEST_CODE);
                        ret.put("value", "sent to printer");
                    }
                    call.success(ret);
                }
            }
        } catch (Exception ex) {
            JSObject ret = new JSObject();
            ret.put("value", "printing failed " + ex.getMessage());
            call.success(ret);
        }
    }

    @PluginMethod()
    public void pay(PluginCall call) {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            boolean found = false;
            share.setType("text/plain");
            List<ResolveInfo> resInfo = this.getContext().getPackageManager().queryIntentActivities(share, 0);
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(PAYMENT_URI) ||
                        info.activityInfo.name.toLowerCase().contains(PAYMENT_URI)) {
                    share.putExtra(Intent.EXTRA_ORIGINATING_URI, call.getString("EXTRA_ORIGINATING_URI"));
                    share.putExtra("TRANSACTION_TYPE", call.getString("TRANSACTION_TYPE"));
                    //share.putExtra("TRANSACTION_TYPE","REVERSE LAST");
                    share.putExtra("AMOUNT", call.getString("AMOUNT")); // 15.00
                    share.putExtra("ADDITIONAL_AMOUNT", call.getString("ADDITIONAL_AMOUNT"));
                    share.putExtra("OPERATOR_ID", call.getString("OPERATOR_ID"));
                    share.putExtra("REFERENCE_NUMBER", call.getString("REFERENCE_NUMBER"));
                    share.putExtra("TRANSACTION_ID", call.getString("TRANSACTION_ID"));
                    lastSentTsn = call.getString("TRANSACTION_ID");
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JSObject ret = new JSObject();
                ret.put("value", "no dashpay pos");
                call.success(ret);
                return;
            }

            mReturnResults = call;
            startActivityForResult(call,Intent.createChooser(share, "Select"), REQUEST_CODE);
            //this.getBridge().getActivity().startActivityForResult(Intent.createChooser(share, "Select"), REQUEST_CODE);
        } catch (Exception ex) {
            JSObject ret = new JSObject();
            ret.put("value", "dashpay pos init failed " + ex.getMessage());
            call.success(ret);
        }
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            Log.d("Test", "No stored plugin call for permissions request result");
            return;
        }

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Log.d("Test", "User denied permission");
                return;
            }
        }

        if (requestCode == PRINT_REQUEST_CODE) {
            // We got the permission!
            loadContacts(savedCall);
        }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if (requestCode == REQUEST_CODE) {
                if (mReturnResults != null) {
                    JSObject ret = new JSObject();

                    if (resultCode == Activity.RESULT_OK) {

                        String tid = intent.getStringExtra("TRANSACTION_ID");
                        String result = intent.getStringExtra("RESULT");

                        if (result.equals("APPROVED")) {
                            //Toast.makeText(getActivity(),intent.getStringExtra("RESPONSE_CODE"),Toast.LENGTH_SHORT).show();
                            String responseCode = intent.getStringExtra("RESPONSE_CODE");
                            String authCode = intent.getStringExtra("AUTH_CODE");

                            ret.put("result", result);

                            ret.put("result", result);
                            ret.put("displayTest", authCode);
                            ret.put("responseCode", responseCode);
                            ret.put("value", "APPROVED");
                            mReturnResults.resolve(ret);

                        } else if (result.equals("DECLINED")) {
                            ret.put("value", "DECLINED");
                            mReturnResults.success(ret);
                        } else {
                            ret.put("value", "FAILED");
                            mReturnResults.success(ret);
                        }

                        new CountDownTimer(2000, 1000) { // 5000 = 5 sec

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                            }
                        }.start();

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        ret.put("value", "Cancelled");
                        mReturnResults.success(ret);
                    }
                }
            }else {
                JSObject ret = new JSObject();
                ret.put("value", "REQUEST_CODE not matching");
                mReturnResults.success(ret);
                /*if(requestCode == PRINT_REQUEST_CODE){
                    String printResult = intent.getStringExtra("RESULT");
                    if(printResult.toLowerCase() == "true") {
                        mReturnResults.resolve(printResult);
                    }else {
                        mReturnResults.reject(printResult);
                    }
                }*/
            }
        }catch (Exception e){
            mReturnResults.reject("bad",e);
        }
    }

    void loadContacts(PluginCall call) {
        ArrayList<Map> contactList = new ArrayList<>();
        ContentResolver cr = this.getContext().getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                map.put("firstName", name);
                map.put("lastName", "");

                String contactNumber = "";

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    pCur.moveToFirst();
                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("phoneNUmber", "The phone number is " + contactNumber);
                }
                map.put("telephone", contactNumber);
                contactList.add(map);
            }
        }
        if (cur != null) {
            cur.close();
        }

        JSONArray jsonArray = new JSONArray(contactList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.success(ret);
    }
}
