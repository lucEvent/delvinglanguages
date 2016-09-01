package com.delvinglanguages.net;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.delvinglanguages.R;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * The use of this class needs the correspondent Activity or Fragment
 * to implement both methods, onActivityResult and onRequestPermissionsResult
 */
public class CredentialsManager {

    private static final int REQUEST_CREDENTIALS = 3214;
    private static final int REQUEST_PERMISSIONS = 9856;

    private static final String PREF_ACCOUNT_KEY = "ACCOUNT_NAME";

    private static final String CLIENT_ID = "server:client_id:917098804320-oches3abrbgdskd18lvl2md73h8kq97s.apps.googleusercontent.com";

    private Context context;
    private GoogleAccountCredential credential;

    public CredentialsManager(Context context)
    {
        this.context = context;
        credential = GoogleAccountCredential.usingAudience(context, CLIENT_ID);
    }

    public GoogleAccountCredential getCredential()
    {
        setUpCredentials();
        return credential;
    }

    public boolean hasCredentials()
    {
        setUpCredentials();
        return credential.getSelectedAccount() != null;
    }

    private void setUpCredentials()
    {
        String accountName = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_ACCOUNT_KEY, null);
        if (accountName != null)
            credential.setSelectedAccountName(accountName);
    }

    public boolean hasPermissions()
    {
        return Build.VERSION.SDK_INT < 23 || ContextCompat.
                checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askPermissions(@Nullable Fragment f)
    {
        if (f != null)
            f.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSIONS);
        else
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSIONS);
    }

    public void askCredentials(@Nullable Fragment f)
    {
        if (f != null)
            f.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_CREDENTIALS);
        else
            ((Activity) context).startActivityForResult(credential.newChooseAccountIntent(), REQUEST_CREDENTIALS);

    }

    /**
     * To call in Activity's or Fragment's onActivityResult after calling askCredentials
     * <p/>
     * #parameters: Same parameters as Activity's or Fragment's onActivityResult
     *
     * @return true if the request was about credentials, false if it was another request
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode != REQUEST_CREDENTIALS)
            return false;

        if (data != null && data.getExtras() != null) {
            String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);

            if (accountName != null) {
                credential.setSelectedAccountName(accountName);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putString(PREF_ACCOUNT_KEY, accountName);
                editor.apply();
            }
        }
        return true;
    }

    /**
     * To call in Activity's or Fragment's onRequestPermissionsResult after calling askPermissions
     * <p/>
     * #parameters: Same parameters as Activity's or Fragment's onRequestPermissionsResult, except permissions
     *
     * @return true if the request was about this permission, false if it was another request
     */
    public boolean onRequestPermissionsResult(int requestCode, int[] grantResults)
    {
        if (requestCode != REQUEST_PERMISSIONS)
            return false;

        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            Toast.makeText(context, R.string.msg_permission_for_login_denied, Toast.LENGTH_LONG).show();

        return true;
    }

}
