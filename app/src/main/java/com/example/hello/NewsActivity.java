package com.example.hello;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity{

        private RecyclerView mrecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mlayoutManager;
        RequestQueue queue;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news);
            mrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mrecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mlayoutManager = new LinearLayoutManager(this);
            mrecyclerView.setLayoutManager(mlayoutManager);

            // specify an adapter (see also next example)


            queue = Volley.newRequestQueue(this);
            getNews();

            // 화면 로딩 -> 뉴스정보 받아온다
            //정보 -> 어댑터
            // 어뎁터 ->셋팅
        }

        public void getNews(){
            // Instantiate the RequestQueue.

            String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=17bffc1987ca4a2a8e6063a2e912265a";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("NEWS",response);


                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray arrayArticles =jsonObj.getJSONArray("articles");

                                List<NewsData> news = new ArrayList<>();
                                for(int i=0,j=arrayArticles.length();i < j;i++){
                                    JSONObject obj = arrayArticles.getJSONObject(i);

                                    Log.d("NEWS", obj.toString());

                                    NewsData newsData=new NewsData();
                                    newsData.setTitle(obj.getString("title"));
                                    newsData.setUrlToImage(obj.getString("urlToImage"));
                                    newsData.setContent(obj.getString("description"));
                                    newsData.setUrl(obj.getString("url"));

                                    news.add(newsData);
                                }



                                mAdapter = new MyAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Object obj = v.getTag();
                                        if(obj!=null){
                                            int position=(int)obj;
                                            String url=(String)(((MyAdapter)mAdapter).getNews(position).getUrl());
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(intent);

                                        }
                                    }
                                });
                                mrecyclerView.setAdapter(mAdapter);

                                //textView.setText("Response is: "+ response.substring(0,500));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("That didn't work!");
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
}
