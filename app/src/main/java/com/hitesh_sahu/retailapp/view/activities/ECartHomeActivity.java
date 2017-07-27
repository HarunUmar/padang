package com.hitesh_sahu.retailapp.view.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hitesh_sahu.retailapp.AppController;
import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.domain.helper.Connectivity;
import com.hitesh_sahu.retailapp.domain.mining.AprioriFrequentItemsetGenerator;
import com.hitesh_sahu.retailapp.domain.mining.FrequentItemsetData;
import com.hitesh_sahu.retailapp.model.CenterRepository;
import com.hitesh_sahu.retailapp.model.entities.Money;
import com.hitesh_sahu.retailapp.model.entities.Product;
import com.hitesh_sahu.retailapp.util.EndpointAPI;
import com.hitesh_sahu.retailapp.util.GPSTracker;
import com.hitesh_sahu.retailapp.util.PreferenceHelper;
import com.hitesh_sahu.retailapp.util.SessionManager;
import com.hitesh_sahu.retailapp.util.Template;
import com.hitesh_sahu.retailapp.util.TinyDB;
import com.hitesh_sahu.retailapp.util.Utils;
import com.hitesh_sahu.retailapp.util.Utils.AnimationType;
import com.hitesh_sahu.retailapp.view.fragment.HomeFragment;
import com.hitesh_sahu.retailapp.view.fragment.WhatsNewDialog;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_BAYAR;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID_KATAGORI;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID_MENU;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID_RM;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID_USERS;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_JAM;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_JUMLAH;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_KET;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_LANGTITUDE;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_LATITUDE;
import static com.hitesh_sahu.retailapp.util.Template.Query.tag_json_obj;

public class ECartHomeActivity extends AppCompatActivity {

    AprioriFrequentItemsetGenerator<String> generator =
            new AprioriFrequentItemsetGenerator<>();
    private static final String TAG = ECartHomeActivity.class.getSimpleName();

    public static final double MINIMUM_SUPPORT = 0.1;
        SessionManager session;
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private DrawerLayout mDrawerLayout;

    private TextView checkOutAmount, itemCountTextView;
    private TextView offerBanner;
    private AVLoadingIndicatorView progressBar;

    private NavigationView mNavigationView;
    int success;
    android.app.AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
   TextView nama;
    TimePicker jam_ok;
    String[] text1 = { "pesan antar", "makan di tempat" };
    String [] val1 = { "pesan antar", "makan di tempat"};
    GPSTracker gps = new GPSTracker(this);
    EditText hasil_spiner;
    double latitude ,longitude;
    private EditText time;
    private Calendar calendar;
    private String format = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();










        setContentView(R.layout.activity_ecart);

        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();

        //	makeFakeVolleyJsonArrayRequest();

        offerBanner = ((TextView) findViewById(R.id.new_offers_banner));

        itemCountTextView = (TextView) findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));

        checkOutAmount = (TextView) findViewById(R.id.checkout_amount);
        checkOutAmount.setSelected(true);
        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
        offerBanner.setSelected(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);



        HashMap<String, String> user = session.getUserDetails();
        String id_nama = user.get(SessionManager.KEY_NAMA);

        View header = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        mNavigationView.addHeaderView(header);
     //  mNavigationView.removeHeaderView(mNavigationView.getHeaderView(0));
        nama = (TextView) header.findViewById(R.id.draw_nama);
        nama.setText(id_nama);



        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);

        checkOutAmount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.vibrate(getApplicationContext());

                Utils.switchContent(R.id.frag_container,
                        Utils.SHOPPING_LIST_TAG, ECartHomeActivity.this,
                        AnimationType.SLIDE_UP);

            }
        });


        if (itemCount != 0) {
            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {

                updateCheckOutAmount(
                        BigDecimal.valueOf(Long.valueOf(product.getSellMRP())),
                        true);
            }
        }

        findViewById(R.id.item_counter).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());
                        Utils.switchContent(R.id.frag_container,
                                Utils.SHOPPING_LIST_TAG,
                                ECartHomeActivity.this, AnimationType.SLIDE_UP);

                    }
                });

        findViewById(R.id.checkout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Utils.vibrate(getApplicationContext());

                        DialogForm("", "", "", "SIMPAN");

                    }
                });

        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new HomeFragment(), this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);

        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.home:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.HOME_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);

                                return true;

                            case R.id.my_cart:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SHOPPING_LIST_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;



                            case R.id.contact_us:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.CONTACT_US_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.settings:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SETTINGS_FRAGMENT_TAG,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;
                            case R.id.info:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.INFO_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.saldo:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SALDO_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;


                            case R.id.keluar:

                                mDrawerLayout.closeDrawers();

                                session.logoutUser();

                                return true;


                            case R.id.history:
                                mDrawerLayout.closeDrawers();
                                Utils.switchContent(R.id.frag_container,
                                        Utils.HISTORY_FRAGMENT,
                                        ECartHomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            default:
                                return true;
                        }
                    }
                });

    }

    public AVLoadingIndicatorView getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        toggleBannerVisibility();
    }

    public void updateCheckOutAmount(BigDecimal amount, boolean increment) {

        if (increment) {
            checkoutAmount = checkoutAmount.add(amount);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
        }

        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Store Shopping Cart in DB
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            final Dialog dialog = new Dialog(ECartHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }

        // Show Whats New Features If Requires
        new WhatsNewDialog(this);
    }

    /*
     * Toggles Between Offer Banner and Checkout Amount. If Cart is Empty SHow
     * Banner else display total amount and item count
     */
    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    /*
     * get total checkout amount
     */
    public BigDecimal getCheckoutAmount() {
        return checkoutAmount;
    }

	/*
     * Makes fake Volley request by adding request in fake Volley Queue and
	 * return mock JSON String plese visit
	 * com.hitesh_sahu.retailapp.domain.mock.FakeHttpStack and
	 * FakeRequestQueue queu
	 */
//	private void makeFakeVolleyJsonArrayRequest() {
//
//		JsonArrayRequest req = new JsonArrayRequest(
//				NetworkConstants.URL_GET_ALL_CATEGORY,
//				new Response.Listener<JSONArray>() {
//					@Override
//					public void onResponse(JSONArray response) {
//						Log.d(TAG,
//
//						response.toString());
//
////						Toast.makeText(getApplicationContext(),
////								"Volley Fake response", Toast.LENGTH_SHORT)
////								.show();
//
//						// hidepDialog();
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						VolleyLog.d(TAG, "Error: " + error.getMessage());
//
//						Log.e(TAG,
//								"------------------------" + error.getMessage());
////						Toast.makeText(getApplicationContext(),
////								error.getMessage(), Toast.LENGTH_SHORT).show();
//					}
//				});
//
//		// Adding request to request queue
//		AppController.getInstance().addToFakeRequestQueue(req);
//	}

    /*
     * Get Number of items in cart
     */
    public int getItemCount() {
        return itemCount;
    }

    /*
     * Get Navigation drawer
     */
    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }





    // untuk menampilkan dialog from kontak
    private void DialogForm(String idx, String namax, String alamatx, String button) {
        dialog = new android.app.AlertDialog.Builder(ECartHomeActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_konfirmasi, null);


        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.app_icon);
        dialog.setTitle("Form Konfirmasi");

        Spinner mySpinner=(Spinner) dialogView.findViewById(R.id.spinner1);
        hasil_spiner = (EditText) dialogView.findViewById(R.id.txt_hasil_spinner);
        mySpinner.setOnItemSelectedListener(onItemSelectedListener1);
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(ECartHomeActivity.this,
                        android.R.layout.simple_spinner_item, text1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter1);



        jam_ok = (TimePicker) dialogView.findViewById(R.id.timePicker1);
        time =  (EditText) dialogView.findViewById(R.id.txt_hasil_time);



        jam_ok.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface


                String jamnya = Integer.toString(hourOfDay);
                String menitnya = Integer.toString(minute);
                time.setText(jamnya + ":" + menitnya);
            }
        });






        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ArrayList<String> productId = new ArrayList<String>();
                ArrayList<String> idKatagori = new ArrayList<String>();
                ArrayList<String> jum = new ArrayList<String>();


                for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                    //add product ids to array

                    productId.add(productFromShoppingList.getProductId());
                    idKatagori.add(productFromShoppingList.getIdKatagori());

                    jum.add(productFromShoppingList.getQuantity());
                   //kirim_pesanan(productFromShoppingList.getProductId(),productFromShoppingList.getIdKatagori(),productFromShoppingList.getQuantity());

                }




                //Toast.makeText(ECartHomeActivity.this,movieList.get(position).getNama(),Toast.LENGTH_LONG).show();
                //pass product id array to Apriori ALGO
                CenterRepository.getCenterRepository()
                        .addToItemSetList(new HashSet<>(productId));
                CenterRepository.getCenterRepository()
                        .addToItemSetList(new HashSet<>(idKatagori));
                CenterRepository.getCenterRepository()
                        .addToItemSetList(new HashSet<>(jum));

                //  Toast.makeText(getApplicationContext(),productId.toString(),Toast.LENGTH_LONG).show();


             //   Log.e("dinda :",productId.toString()+"|"+idKatagori.toString());

                HashMap<String, String> user = session.getUserDetails();
                String id_rm = user.get(SessionManager.KEY_ID_RM);
                String id_user = user.get(SessionManager.KEY_ID);
                final String bayar = checkoutAmount.toString();
                final String ket =  hasil_spiner.getText().toString();
                final String jam = time.getText().toString();
                // Enable MyLocation Layer of Google Map
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


                // Get LocationManager object from System Service LOCATION_SERVICE

                // Create a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Get the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Get Current Location
                Location myLocation = locationManager.getLastKnownLocation(provider);





                if(myLocation != null) {
                    // Get latitude of the current location
                    latitude = myLocation.getLatitude();

                    // Get longitude of the current location
                    longitude = myLocation.getLongitude();
                    // Create a LatLng object for the current location
                    String latx =  String.valueOf(latitude).toString();
                    String longx = String.valueOf(longitude).toString();

                    Log.e("dinda",latx);
                    kirim_pesanan(productId.toString(),idKatagori.toString(),jum.toString(),id_rm.toString(),id_user.toString(),bayar, jam,ket,latx, longx);

                }
                else  {

                    gps.showSettingsAlert();
                }



                //Do Minning
                FrequentItemsetData<String> data = generator.generate(
                        CenterRepository.getCenterRepository().getItemSetList()
                        , MINIMUM_SUPPORT);

                for (Set<String> itemset : data.getFrequentItemsetList()) {
                    Log.e("APriori", "Item Set : " +
                            itemset + "Support : " +
                            data.getSupport(itemset));
                }

                //clear all list item
                CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();

                toggleBannerVisibility();

                itemCount = 0;
                itemCountTextView.setText(String.valueOf(0));
                checkoutAmount = new BigDecimal(BigInteger.ZERO);
                checkOutAmount.setText(Money.rupees(checkoutAmount).toString());


                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                kosong();
            }
        });

        dialog.show();
    }


    private void kirim_pesanan(final String id_menu, final String id_katagori,final String jumlah,final String id_rm,final String id_user, final String bayar, final String jam,final String ket, final String lat, final String lang ){
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Mengirim...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndpointAPI.AND_PESANAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(Template.Query.TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("v Add", jObj.toString());

                                Toast.makeText(ECartHomeActivity.this, jObj.getString(Template.Query.TAG_MESSAGE), Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(ECartHomeActivity.this, jObj.getString(Template.Query.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(ECartHomeActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, error.getMessage().toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String,String> params = new HashMap<String, String>();




                //menambah parameter yang di kirim ke web servis
                params.put(TAG_ID_MENU, id_menu);
                params.put(TAG_ID_KATAGORI,id_katagori);
                params.put(TAG_ID_RM,id_rm);
                params.put(TAG_ID_USERS,id_user);
                params.put(TAG_JUMLAH,jumlah);
                params.put(TAG_BAYAR,bayar);
                params.put(TAG_KET,ket);
                params.put(TAG_JAM,jam);
                params.put(TAG_LATITUDE,lat);
                params.put(TAG_LANGTITUDE,lang);


                //kembali ke parameters
                Log.d(TAG, ""+params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener()

    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String s1 = String.valueOf(val1[position]);
            hasil_spiner.setText(s1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

    };


    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ECartHomeActivity.this);
        alertDialogBuilder.setMessage("GPS tidak aktif")
                .setCancelable(false)
                .setPositiveButton("Aktifkan GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void setTime(View view) {
        int hour = jam_ok.getCurrentHour();
        int min = jam_ok.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min));
    }




}
