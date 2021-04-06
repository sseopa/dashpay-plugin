package coza.rubiblue.dashpay;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
 
import org.json.JSONArray;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NativePlugin(
        requestCodes={DashPayModule.PRINT_REQUEST_CODE}
)
public class DashPayModule extends Plugin {
    private static Context ionicContext;
    public DashPayModule(Context ctx)
    {
        super(); // Remove this
        this.ionicContext = ctx;

    }
protected static final int PRINT_REQUEST_CODE = 2; // Unique request code
    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }
    
 
    @PluginMethod()
    public void print(PluginCall call) {

        String printString = call.getString("printString");
        String EXTRA_ORIGINATING_URI = call.getString("EXTRA_ORIGINATING_URI");
        // Filter based on the value if want
        Intent share = new Intent(Intent.ACTION_SEND);
    boolean found = false;
    share.setType("text/plain");
    List<ResolveInfo> resInfo = ionicContext.getPackageManager().queryIntentActivities(share, 0);
    for (ResolveInfo info : resInfo) {
      if (info.activityInfo.packageName.toLowerCase().contains("com.dashpay.bridge") ||
              info.activityInfo.name.toLowerCase().contains("com.dashpay.bridge") ) {
        share.putExtra(Intent.EXTRA_ORIGINATING_URI, EXTRA_ORIGINATING_URI);
        share.putExtra("key", "Print");
        share.putExtra("printString", printString);
        share.setPackage(info.activityInfo.packageName);
        found = true;
        break;
      }
    }
    if (!found){
      //promise.resolve("com.dashpay.bridge");
        JSObject ret = new JSObject();
        ret.put("value", "printing failed, bridge app not detected");
        call.success(ret);
      return;
    }
    //mReturnResults = promise;
        startActivityForResult(call,Intent.createChooser(share,"Select"),PRINT_REQUEST_CODE);
       JSObject ret = new JSObject();
        ret.put("value", "sent to printer");
        call.success(ret);
        //saveCall(call);
        //pluginRequestPermission(Manifest.permission.READ_CONTACTS, PRINT_REQUEST_CODE);
    }
    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);
 
 
        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            Log.d("Test", "No stored plugin call for permissions request result");
            return;
        }
 
        for(int result : grantResults) {
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
    void loadContacts(PluginCall call) {
        ArrayList<Map> contactList = new ArrayList<>();
        ContentResolver cr = this.getContext().getContentResolver();
 
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                Map<String,String> map =  new HashMap<String, String>();
 
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
 
                map.put("firstName", name);
                map.put("lastName", "");
 
                String contactNumber = "";
 
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    pCur.moveToFirst();
                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("phoneNUmber", "The phone number is "+ contactNumber);
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
