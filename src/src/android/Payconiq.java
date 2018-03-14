package be.happli.cordova.payconiq;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.content.Context;
import android.app.Activity;
/**
 * This class echoes a string called from JavaScript.
 */
public class Payconiq extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("pay")) {
            String transactionId = args.getString(0);
            String returnUrl=args.getString(1);
            this.pay(transactionId,returnUrl, callbackContext);
            return true;
        }
        return false;
    }

    private void pay(String transactionId, String returnUrl, CallbackContext callbackContext) {
        if (transactionId != null && transactionId.length() > 0
        && returnUrl != null && returnUrl.length() > 0) {
            openPayconiq(transactionId,returnUrl);
        } else {
            callbackContext.error("Expected two non-empty string argument for calling Payconiq.");
        }
    }
      private static final String payconiqScheme = "payconiq://payconiq.com/pay/1/%1$s?returnUrl=%2$s";
    private static final String payconiqPlayStore = "https://play.google.com/store/apps/details?id=com.payconiq.customers";

    private void openPayconiq(String transactionId, String returnUrl) {
        //openNewActivity();
        Intent payconiqIntent = getPayconiqIntent(transactionId, returnUrl);
        Activity activity=cordova.getActivity();
        // Check if the Payconiq app is installed
        PackageManager packageManager = activity.getPackageManager();
        if (payconiqIntent.resolveActivity(packageManager) != null) {
            activity.startActivity(payconiqIntent);
        } else {
            // Payconiq app is not installed, show Payconiq in Play Store
            Intent playStoreIntent = getPayconiqPlayStoreIntent();
            activity.startActivity(playStoreIntent);
        }
    }

    private static Intent getPayconiqIntent(String transactionId, String returnUrl) {
        Uri uri = Uri.parse(String.format(payconiqScheme, transactionId, returnUrl));
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private static Intent getPayconiqPlayStoreIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(payconiqPlayStore));
    }

    private void openNewActivity() {
        Context context = cordova.getActivity().getApplicationContext();
        Intent intent = new Intent(context, be.happli.cordova.payconiq.NewActivity.class);
        this.cordova.getActivity().startActivity(intent);
    }
}
