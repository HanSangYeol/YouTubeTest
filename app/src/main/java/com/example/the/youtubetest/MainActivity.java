package com.example.the.youtubetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity {

    static DrawableManager DM = new DrawableManager();
    final String serverKey = "AIzaSyD-ie2ywfIybH6zkUAiFpTGvMrdLb_sTtQ"; //https://console.developers.google.com
    ArrayList<SearchData> sdata = new ArrayList<SearchData>();
    AsyncTask<?, ?, ?> searchTask;
    private EditText eturl;
    private Button search;
    private ListView searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.searchlist = (ListView) findViewById(R.id.searchlist);
        this.search = (Button) findViewById(R.id.search);
        this.eturl = (EditText) findViewById(R.id.eturl);

        eturl = (EditText) findViewById(R.id.eturl);

        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTask = new searchTask().execute();
            }
        });
    }

    private class searchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                paringJsonData(getUtube());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            ListView searchlist = (ListView) findViewById(R.id.searchlist);

            StoreListAdapter mAdapter = new StoreListAdapter(MainActivity.this,
                    R.layout.activity_start, sdata);

            searchlist.setAdapter(mAdapter);

        }
    }

    public JSONObject getUtube() {

        HttpGet httpGet = new HttpGet(
                "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=유튜브채널id&key=" + serverKey
        );
        // part(snippet), q(검색값) , key(서버키)
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    // 파싱을 하면 여러가지 값을 얻을 수 있는데 필요한 값들을 세팅하셔서 사용하시면 됩니다.
    private void paringJsonData(JSONObject jsonObject) throws JSONException {
        sdata.clear();

        JSONArray contacts = jsonObject.getJSONArray("items");

        for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);
            //String vodid = c.getJSONObject("id").getString("videoId");
            String title = c.getJSONObject("snippet").getString("title");
            String changString = "";
            try {
                changString = new String(title.getBytes("8859_1"), "utf-8");

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String date = c.getJSONObject("snippet").getString("publishedAt") // 등록날짜
                    .substring(0, 10);
            String imgUrl = c.getJSONObject("snippet")
                    .getJSONObject("thumbnails").getJSONObject("default")
                    .getString("url"); // 썸내일 이미지 URL값

            Log.d("Youtube", "제목 : " + title);
            sdata.add(new SearchData("", changString, imgUrl, date));

        }
    }

    public class StoreListAdapter extends ArrayAdapter<SearchData> {
        private ArrayList<SearchData> items;
        SearchData fInfo;

        public StoreListAdapter(Context context, int textViewResourseId,
                                ArrayList<SearchData> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {// listview

            // 출력
            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.listview_start, null);
            ImageView img = (ImageView) v.findViewById(R.id.img);

            String url = fInfo.getUrl();

            String sUrl = "";
            String eUrl = "";
            sUrl = url.substring(0, url.lastIndexOf("/") + 1);
            eUrl = url.substring(url.lastIndexOf("/") + 1, url.length());
            try {
                eUrl = URLEncoder.encode(eUrl, "EUC-KR").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String new_url = sUrl + eUrl;

            DM.fetchDrawableOnThread(new_url, img);  //비동기 이미지 로더

            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();

                    Intent intent = new Intent(MainActivity.this,
                            StartActivity.class);
                    intent.putExtra("id", items.get(pos).getVideoId());
                    startActivity(intent);
                }
            });

            ((TextView) v.findViewById(R.id.title)).setText(fInfo.getTitle());
            ((TextView) v.findViewById(R.id.date)).setText(fInfo
                    .getPublishedAt());

            return v;
        }
    }
}

