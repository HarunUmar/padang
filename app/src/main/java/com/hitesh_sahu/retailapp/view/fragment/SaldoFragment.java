package com.hitesh_sahu.retailapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hitesh_sahu.retailapp.AppController;
import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.util.SessionManager;
import com.hitesh_sahu.retailapp.view.activities.ECartHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hitesh_sahu.retailapp.util.EndpointAPI.URL_SALDO;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_NAMA;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_SALDO;
import static com.hitesh_sahu.retailapp.util.Template.Query.tag_json_obj;

// TODO: Auto-generated Javadoc
/**
 * Fragment that appears in the "content_frame", shows a animal.
 */
public class SaldoFragment extends Fragment  {

    private TextView submitLog;
    private Toolbar mToolbar;
    private FragmentActivity myContext;
    private TextView saldo,nama;
    SessionManager session;
    private static final String TAG = ECartHomeActivity.class.getSimpleName();



    public SaldoFragment() {
        // Empty constructor required for fragment subclasses
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saldo, container,
                false);

        getActivity().setTitle("Profil");

        mToolbar = (Toolbar) rootView.findViewById(R.id.htab_toolbar);
        saldo = (TextView) rootView.findViewById(R.id.txt_saldo);
        nama = (TextView) rootView.findViewById(R.id.txt_nama);
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

        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String id_rm = user.get(SessionManager.KEY_ID_RM);
        String id_user = user.get(SessionManager.KEY_ID);

        getSaldo(id_user);


        return rootView;

    }

    public static Fragment newInstance() {
        // TODO Auto-generated method stub
        return new SettingsFragment();
    }



    private void getSaldo(final String id){


        StringRequest strReq = new StringRequest(Request.Method.POST, URL_SALDO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());

                try {
                    JSONObject obj = new JSONObject(response);

                    String Saldo    = obj.getString(TAG_SALDO);
                    String Nama    = obj.getString(TAG_NAMA);

                    saldo.setText(Saldo);
                    nama.setText(Nama);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail saldo: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }




}
