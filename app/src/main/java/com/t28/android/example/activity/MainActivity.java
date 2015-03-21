package com.t28.android.example.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.t28.android.example.R;
import com.t28.android.example.api.request.FeedRequest;
import com.t28.android.example.data.model.Feed;
import com.t28.android.example.fragment.EntryListFragment;
import com.t28.android.example.volley.VolleyHolder;

import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new EntryListFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Request<Feed> request = new FeedRequest.Builder("http://feeds.feedburner.com/hatena/b/hotentry")
                .setNumber(100)
                .setSoftTimeToLive(TimeUnit.MINUTES.toMillis(10))
                .setTimeToLive(TimeUnit.HOURS.toMillis(1))
                .setListener(new Response.Listener<Feed>() {
                    @Override
                    public void onResponse(Feed response) {
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
                .build();
        VolleyHolder.getRequestQueue(this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
