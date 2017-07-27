package com.hitesh_sahu.retailapp.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hitesh_sahu.retailapp.AppController;
import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.model.entities.HistoryData;
import com.hitesh_sahu.retailapp.util.SessionManager;
import com.hitesh_sahu.retailapp.view.activities.ECartHomeActivity;
import com.hitesh_sahu.retailapp.view.adapter.HistoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hitesh_sahu.retailapp.util.EndpointAPI.URL_HISTORY;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_NO;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_STATUS;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_TGL;

// TODO: Auto-generated Javadoc
/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView list;
    private TextView submitLog;
    private Toolbar mToolbar;
    SessionManager session;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();
    List<HistoryData> historyList = new ArrayList<HistoryData>();
    HistoryAdapter adapter;
    SwipeRefreshLayout swipe;
    Handler handler;
    Runnable runnable;

    private static String url_list 	 = URL_HISTORY + "?offset=";

    private int offSet = 0;


    int no;

    public HistoryFragment() {
        // Empty constructor required for fragment subclasses
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container,
                false);

        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String id_rm = user.get(SessionManager.KEY_ID_RM);
       final String id_user = user.get(SessionManager.KEY_ID);

        historyList.clear();

        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        mToolbar.setTitleTextColor(Color.WHITE);



        getActivity().setTitle("History Pemesanan");




        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        list  = (ListView) rootView.findViewById(R.id.list_history);



        adapter = new HistoryAdapter(getActivity(), historyList);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ECartHomeActivity.class);
                intent.putExtra(TAG_ID, historyList.get(position).getId());
                startActivity(intent);
            }
        });


        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           historyList.clear();
                           adapter.notifyDataSetChanged();
                           callHistory(0,id_user);
                       }
                   }
        );

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                           callHistory(offSet,id_user);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });


        return rootView;

    }

    @Override
    public void onRefresh() {
        historyList.clear();
        adapter.notifyDataSetChanged();
        HashMap<String, String> user = session.getUserDetails();
        String id_rm = user.get(SessionManager.KEY_ID_RM);
        final String id_user = user.get(SessionManager.KEY_ID);
       callHistory(0,id_user);
    }


    private void callHistory(int page, String idx){

        swipe.setRefreshing(true);

        // Creating volley request obj
        JsonArrayRequest arrReq = new JsonArrayRequest(url_list + page+"&user="+idx,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    HistoryData  his= new HistoryData();

                                    no = obj.getInt(TAG_NO);

                                    his.setId(obj.getString(TAG_ID));
                                    his.setTgl(obj.getString(TAG_TGL));
                                    his.setStatus(obj.getString(TAG_STATUS));



                                    // adding news to news array
                                    historyList.add(his);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.d(TAG, "offSet " + offSet);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Fragment newInstance() {
        // TODO Auto-generated method stub
        return new SettingsFragment();
    }








}
