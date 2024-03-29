package com.example.loginapp;

import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements  AuthenticationListener {

    private static int RC_SIGN_IN = 100;
    private String token = null;
    AppPreferences appPreferences = null;
    ImageView profilePhoto,IVGogglelogo,IVFacebooklogo,IVInstagramlogo,IVTwitterlogo;
    TextView txtUserid,txtUsername;
    EditText edtEmail,edtPassword;
    Button btnLogin;
    TextView txtSignuptv;
    String email_login,password_login;
    GoogleSignInClient mGoogleSignInClient; // FOR GOOGLE
    CallbackManager callbackManager; // FOR FACEBOOK
    AuthenticationDialog  authenticationDialog = null;  // FOR INSTAGRAM

    Spinner spinner;
    public static final String[] languages = {"select language","Hindi","English"};
    public AuthenticationListener listener;

    String access_token;
    FirebaseAuth firebaseAuth;  // FOR TWITTER

    AlertDialog.Builder builder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      //  firebaseAuth = FirebaseAuth.getInstance();  // FOR TWITTER


        initView();
        FaceBookManager();

        ClickEventGoogle();
        ClickEventFacebook();
        ClickEventInstagram();
        ClickEventTextView();
        GoogleConfiguration();
        ClickEventLoginbutton();
        SpinnerForLanguage();
        ClickEventTwitter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }
    private void ClickEventTwitter() {

        IVTwitterlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
                    provider.addCustomParameter("lang", "fr");

                    Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
                    if (pendingResultTask != null) {
                        // There's something already here! Finish the sign-in for your user.
                        pendingResultTask
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                Intent i =new Intent(LoginActivity.this,Welcome_Main.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                showToast("Exception : "+e);
                                            }
                                        });
                    } else
                    {
                        firebaseAuth
                                .startActivityForSignInWithProvider(/* activity= */ LoginActivity.this, provider.build())
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                Intent i =new Intent(LoginActivity.this,Welcome_Main.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                showToast("Exception 2 "+e);
                                            }
                                        });
                    }
                }
                catch (Exception ex)
                {
                    showToast("Exception : "+ex);
                }
            }
        });
    }
    private void initView() {
        profilePhoto =findViewById(R.id.profilephoto);

        txtUserid = findViewById(R.id.user_id);
        txtUsername = findViewById(R.id.user_name);

        edtEmail =findViewById(R.id.edittext_emailaddress_login);
        edtPassword =findViewById(R.id.edittextpassword);

        txtSignuptv =findViewById(R.id.linkforlogin2);

        btnLogin =findViewById(R.id.login_button);

        IVGogglelogo =findViewById(R.id.googlelogo);
        IVFacebooklogo =findViewById(R.id.facebooklogo);
        IVInstagramlogo = findViewById(R.id.instagramlogo);
        IVTwitterlogo = findViewById(R.id.twitterlogo);
        spinner = findViewById(R.id.spinner_language);

        builder = new AlertDialog.Builder(this);

    }
    private void SpinnerForLanguage() {
        try
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,languages);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selectedLang = parent.getItemAtPosition(position).toString();

                    if(selectedLang.equals("English"))
                    {
                        setLocal(LoginActivity.this,"en");
                        finish();
                        startActivity(getIntent());
                    }
                    else if (selectedLang.equals("Hindi"))
                    {
                        setLocal(LoginActivity.this,"hi");
                        finish();
                        startActivity(getIntent());
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (Exception ex)
        {
            showToast("Exception : "+ex);
        }
    }
    public  void setLocal(Activity activity,String langCode)
    {
        try
        {
            Locale locale = new  Locale(langCode);
            locale.setDefault(locale);
            Resources resources = activity.getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config,resources.getDisplayMetrics());
        }
        catch (Exception ex)
        {
            showToast("Exception 2 : "+ex);
        }

    }
    private void ClickEventLoginbutton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {  // CLICK EVENT OF SIGN IN (LOG IN)
            @Override
            public void onClick(View v) {
                try
                {
                    email_login = edtEmail.getText().toString().trim();
                    password_login = edtPassword.getText().toString().trim();

                    if(email_login.length() > 0 && password_login.length() > 0)
                    {
                        if(Patterns.EMAIL_ADDRESS.matcher(email_login).matches())
                        {
                            // valid email address
                            if(isValidPassword(password_login))
                            {
                                if(password_login.length() > 7 && password_login.length() < 25)
                                {
                                    tokenForAuthenticate();
                                }
                                else
                                {
                                    showToast("Password Length should be more then 7 and less than 25");
                                }
                            }
                            else
                            {
                                showToast("Please Enter Valid Password");
                            }
                        }
                        else
                        {
                            showToast("Please Enter valid Email address");
                        }
                    }
                    else
                    {
                        showToast("Please Enter All Details");
                    }
                }
                catch(Exception e)
                {
                    showToast("Please Enter All Details"+e);
                }

            }
        });
    }
    private void GoogleConfiguration() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }
    private void ClickEventTextView() {
        txtSignuptv.setOnClickListener(new View.OnClickListener() {  // CLICK EVENT OF SIGN UP (LINK OF SIGN UP)
            @Override
            public void onClick(View v) {

                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }
    private void ClickEventInstagram() {
        IVInstagramlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    authenticationDialog = new AuthenticationDialog(LoginActivity.this,
                            listener);
                    authenticationDialog.setCancelable(true);
                    authenticationDialog.show();
                }
                catch (Exception ex)
                {
                    showToast("Error 1 "+ex);
                }
            }
        });
    }
    private void ClickEventFacebook() {
        IVFacebooklogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                //    finish();
            }
        });
    }
    private void ClickEventGoogle() {
        IVGogglelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    private void FaceBookManager() {
        // FOR FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        startActivity(new Intent(LoginActivity.this,Welcome_Main.class));

                        showToast("Log In Successfully");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }
    private void postLoginData(String access_token)
    {
        try
        {
            String url = "https://admin.p9bistro.com/index.php/SignIn";

            // Sending Request to server...,
            JSONObject req = new JSONObject();
            try
            {
                req.put("username",email_login);
                req.put("password",password_login);
                req.put("login_by",5);
            }
            catch(Exception ex)
            {
                showToast("Exception "+ex);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,req,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        if(response.getBoolean("status"))
                        {
                            String message = response.getString("message");
                            showToast(message);
                            JSONObject resobj = response.getJSONObject("data");
                            String id = resobj.getString("id");
                            String username = resobj.getString("username");
                            String mobile_no = resobj.getString("mobile_no");
                            String email2 = resobj.getString("email");
                            String profile = resobj.getString("profile");
                            String api_key = resobj.getString("api_key");
                            Log.e("Login api key","Login api key");
                            Log.e("LOGIN API KEY : ",api_key);

                            SharedPreferences sharedPreferences =getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor edit =sharedPreferences.edit();
                            edit.putString("apiKey",api_key);
                            edit.apply();

                            edtEmail.setText("");
                            edtPassword.setText("");

                            Intent i = new Intent(LoginActivity.this, Dashboard_Activity_p9.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    showToast("Fail to get Response : "+error);
                }
            }){
                @Override
                public Map<String,String> getHeaders()throws AuthFailureError
                {
                    Map<String,String>params = new HashMap<String ,String>();
                    params.put("authorization",access_token);
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }
        catch (Exception ex)
        {
            Toast.makeText(LoginActivity.this, "Error 3 : "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void tokenForAuthenticate()
    {
        String url = "https://admin.p9bistro.com/index.php/generate_auth_token";
        Log.e("checklog", url + "");

        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("checklog", response + "");
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(response);
                    access_token = jsonObject.getString("access_token");

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyToken", MODE_PRIVATE);
                    SharedPreferences.Editor edit =sharedPreferences.edit();
                    edit.putString("token",access_token);
                    edit.apply();

                    Log.e("ACCESSTOKEN", access_token);
                    postLoginData(access_token);

                } catch (JSONException je) {
                    Toast.makeText(LoginActivity.this, "Error 2 : " + je, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("checklog",error + "");
                Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String,String> headers = new HashMap<>();
                headers.put("x-api-key","XABRTYUX@123YTUFGB");
                return headers;
            }
        };
        RequestQueue requestquese = Volley.newRequestQueue(getApplicationContext());
        requestquese.add(stringRequest);
    }
    public static boolean isValidPassword(final String password)  // 1 number , 1 Uppercase , 1 special symbol , 1 lowercase
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);  // FOR FACEBOOK INTEGRATION

        // FOR GOOGLE LOGIN
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
         //   GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if(acct != null)
            {
                String personGivenName = acct.getEmail();
                showToast("Welcome "+personGivenName);
                startActivity(new Intent(LoginActivity.this,Welcome_Main.class));
            }
            else {
                showToast("NULL");
            }
            // Signed in successfully, show authenticated UI.

        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "Message" + e.toString());

        }
    }
    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        getUserInfoByAccessToken(token);
    }
    private void getUserInfoByAccessToken(String token)
    {
        try {
            new RequestInstagramAPI().execute();
        }
        catch (Exception ex)
        {
            showToast("Error 2 "+ex);
        }
    }
    private class RequestInstagramAPI extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            if(response != null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));
                        login();
                    }
                }
                catch(JSONException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                showToast("Login Error!");
            }
        }
    }
    public void login()  // FOR INSTAGRAM
    {
        try {
            Picasso.get().load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(profilePhoto);
            txtUserid.setText(appPreferences.getString(AppPreferences.USER_ID));
            txtUsername.setText(appPreferences.getString(AppPreferences.USER_NAME));
        }
        catch (Exception ex)
        {
            showToast("Exception : "+ex);
        }
    }
    @Override
    public void onBackPressed() {



        builder.setMessage("Do you want to Exit ?").setTitle("Do you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                      //  Toast.makeText(getApplicationContext(), "you choose no action for alertbox", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();
    }
}