package com.ankit.channe.vprint;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    TextView t2;

    EditText urlText;
    static final String COOKIES_HEADER = "Set-Cookie";
    static final String COOKIES_HEADER1 = "Cookie";

    static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    static java.net.CookieManager msCookieManager1 = new java.net.CookieManager();
    private static final String POST_PARAMS = "service=direct%2F1%2FUserWebPrintSelectPrinter%2F%24Form&sp=S0&Form0=%24Hidden%2C%24Hidden%240%2C%24TextField%2C%24Submit%2C%24RadioGroup%2C%24Submit%240%2C%24Submit%241&%24Hidden=&%24Hidden%240=&%24TextField=&%24RadioGroup=24&%24Submit%241=2.+Print+Options+and+Account+Selection+%C2%BB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t2 = (TextView) findViewById(R.id.textView2);
        t2.setMovementMethod(new ScrollingMovementMethod());
    }

    public void check(View v) {


        urlText = (EditText) findViewById(R.id.editText);
        String stringUrl = "http://" + urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            new DownloadWebpageTask().execute(stringUrl);
        } else {
            t2.setText("We're not connected");
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override

        protected String doInBackground(String... urls) {
            try {

                return downloadUrl("http://vprint.vit.edu.in:9191/app?service=restart");


            } catch (IOException e) {

                Log.e("TAG", "Exception: " + Log.getStackTraceString(e));

                return "Unable to retrieve the url.";


            }
        }

        protected void onPostExecute(String result) {

            t2.setText(result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = 5000;
        try {


            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "keep-alive");
            // Starts the query
            conn.connect();
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
            System.out.println("First header"+headerFields);
            System.out.println("cookies header: "+cookiesHeader);

            if(cookiesHeader != null)
            {
                System.out.println("inside cookie");
                for (String cookie : cookiesHeader)
                {
                    msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }
            int response = conn.getResponseCode();
            Log.d("Response", "The response is: " + response);
            URL login = new URL("http://vprint.vit.edu.in:9191/app?service=direct%2F1%2FHome%2F%24Form%240&sp=S0&Form0=%24Hidden%240%2C%24Hidden%241%2CinputUsername%2CinputPassword%2C%24PropertySelection%240%2C%24Submit%240&%24Hidden%240=true&%24Hidden%241=X&inputUsername=14101A0057&inputPassword="+/*Your password*/"&%24PropertySelection%240=en&%24Submit%240=Log+in");
            HttpURLConnection conn1 =(HttpURLConnection)login.openConnection();
            conn1.setReadTimeout(10000 /* milliseconds */);
            conn1.setConnectTimeout(15000 /* milliseconds */);
            conn1.setRequestMethod("GET");
            conn1.setDoInput(true);
            if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {
                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn1.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            conn1.setRequestProperty("Connection","keep-alive");
            // Starts the query
            conn1.connect();
            Map<String, List<String>> headerFields1 = conn1.getHeaderFields();
            List<String> cookiesHeader1 = headerFields1.get(COOKIES_HEADER);

            System.out.println("cookie headers are" + cookiesHeader1);

            if(cookiesHeader1 != null)
            {
                System.out.println("inside cookie apache");
                for (String cookie : cookiesHeader1)
                {
                    msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }
            System.out.println("Second header:" + conn1.getHeaderFields());
            System.out.println("Cookies are: "+msCookieManager.getCookieStore().getCookies());





            int response1 = conn1.getResponseCode();
            Log.d("Response", "The response is: " + response1);


            URL webprint = new URL("http://vprint.vit.edu.in:9191/app?service=page/UserWebPrint");
            HttpURLConnection conn2 = (HttpURLConnection)webprint.openConnection();
            conn2.setReadTimeout(10000);
            conn2.setRequestMethod("GET");
            conn2.setDoInput(true);
            conn2.setConnectTimeout(15000);

            conn2.setRequestProperty("Accept-Language","en-US,en;q=0.8");
            conn2.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");




            if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {
                System.out.println("inside cookies2");
                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn2.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                System.out.println(msCookieManager.getCookieStore().getCookies());
            }
            // Starts the query
            conn2.setRequestProperty("Connection","keep-alive");
            conn2.connect();





            URL printSelector = new URL("http://vprint.vit.edu.in:9191/app?service=action/1/UserWebPrint/0/$ActionLink");
            HttpURLConnection conn3 = (HttpURLConnection)printSelector.openConnection();
            conn3.setReadTimeout(10000);
            conn3.setRequestMethod("GET");
            conn3.setDoInput(true);
            conn3.setConnectTimeout(15000);
            conn3.setRequestProperty("Connection","keep-alive");
            conn3.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn3.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");






            if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {
                System.out.println("Inside conn3");
                System.out.println(msCookieManager.getCookieStore().getCookies());

                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn3.setRequestProperty("Cookie",TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            conn3.setRequestProperty("Connection","keep-alive");
            // Starts the query
            conn3.connect();

            URL selectionDone = new URL("http://vprint.vit.edu.in:9191/app");
            HttpURLConnection conn4 = (HttpURLConnection)selectionDone.openConnection();
            conn4.setRequestMethod("POST");
            conn4.setConnectTimeout(15000);
            conn4.setDoInput(true);
            conn4.setRequestProperty("Host", "vprint.vit.edu.in:9191");
            conn4.setDoOutput(true);
            conn4.setRequestProperty("Connection", "keep-alive");
           // conn4.setRequestProperty("Content-Length","286");
            conn4.setRequestProperty("Cache-Control","max-age=0");

            conn4.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn4.setRequestProperty("Origin","http://vprint.vit.edu.in:9191");
            conn4.setRequestProperty("Accept-Language", "en-US,en;q=0.8");

            if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {

                System.out.println("Cookie"+TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));

                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn4.setRequestProperty("Cookie",TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }

            conn4.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn4.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");


           conn4.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            
            conn4.setReadTimeout(10000);






            conn4.setRequestProperty("Referer","http://vprint.vit.edu.in:9191/app?service=action/1/UserWebPrint/0/$ActionLink");





          /* if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {
                System.out.println("Inside conn3");
                System.out.println(msCookieManager.getCookieStore().getCookies());

                //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
                conn4.setRequestProperty("Cookie",TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }*/


            // Starts the query
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("service", "direct/1/UserWebPrintSelectPrinter/$Form")
                    .appendQueryParameter("sp","S0")
                    .appendQueryParameter("Form0", "$Hidden,$Hidden$0,$TextField,$Submit,$RadioGroup,$Submit$0,$Submit$1")
                    .appendQueryParameter("$RadioGroup","24")
                    .appendQueryParameter("$Submit$1","2. Print Options and Account Selection »")
                    .appendQueryParameter("$Hidden$0","")
                    .appendQueryParameter("$Hidden","")
                    .appendQueryParameter("$TextField","");

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn4.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            System.out.println(query);
            conn4.connect();



            System.out.println(conn4.getHeaderFields());














            is = conn4.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);



            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }







    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }







}



