package com.hitesh_sahu.retailapp.domain.mock;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hitesh_sahu.retailapp.AppController;
import com.hitesh_sahu.retailapp.model.CenterRepository;
import com.hitesh_sahu.retailapp.model.entities.Product;
import com.hitesh_sahu.retailapp.model.entities.ProductCategoryModel;
import com.hitesh_sahu.retailapp.util.EndpointAPI;
import com.hitesh_sahu.retailapp.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_HARGA;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_ID_KATAGORI;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_IMAGE;
import static com.hitesh_sahu.retailapp.util.Template.Query.TAG_NAMA;

/*
 * This class serve as fake server and provides dummy product and category with real Image Urls taken from flipkart
 */
public class FakeWebServer {

	private static FakeWebServer fakeServer;
	private static final String url_makanan = EndpointAPI.URL_LIST_MAKANAN;
	private static final String url_minuman = EndpointAPI.URL_LIST_MINUMAN;
	private static final String url_nasikotak = EndpointAPI.URL_LIST_NASIKOTAK;
	private static final String TAG = FakeWebServer.class.getSimpleName();
	void initiateFakeServer() {

		addCategory();

	}
	public void addCategory() {

		ArrayList<ProductCategoryModel> listOfCategory = new ArrayList<ProductCategoryModel>();





		listOfCategory
				.add(new ProductCategoryModel(
						"Makanan",
						"Menu Makanan",
						"10%",
						"http://tarsiustech.com/padang/duta_minang/menu_makanan/rendang.jpg"));

		listOfCategory
				.add(new ProductCategoryModel(
						"Minuman",
						"Menu Minuman",
						"15%",
						"http://tarsiustech.com/padang/padang_raya/menu_minuman/jus_jeruk.png"));

		listOfCategory
				.add(new ProductCategoryModel(
						"Paket",
						"Menu Paket",
						"10%",
						"http://tarsiustech.com/padang/duta_minang/menu_paket/nasi_kotak.jpg"));



		CenterRepository.getCenterRepository().setListOfCategory(listOfCategory);
	}




	public void getAllElectronic() {
		SessionManager session;
		session = new SessionManager(AppController.getcontext());
		HashMap<String, String> user = session.getUserDetails();
		String id_rm = user.get(SessionManager.KEY_ID_RM);
		String id_user = user.get(SessionManager.KEY_ID);



		ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();

		final ArrayList<Product> productlist = new ArrayList<Product>();



		JsonArrayRequest movieReq = new JsonArrayRequest(url_makanan,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								String  DInda = obj.getString(TAG_NAMA);
								String Harga = obj.getString(TAG_HARGA);
								String Image = obj.getString(TAG_IMAGE);
								String id= obj.getString(TAG_ID);
								String id_katagori= obj.getString(TAG_ID_KATAGORI);
								productlist.add(new Product(DInda,DInda,DInda,Harga,"10",Harga,"0",Image,id,id_katagori));

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});



		AppController.getInstance().addToRequestQueue(movieReq);
		productMap.put("List Menu Makanan", productlist);
		CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);

	}



	public void getAllFurnitures() {

		ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();
		SessionManager session;
		session = new SessionManager(AppController.getcontext());
		HashMap<String, String> user = session.getUserDetails();
		String id_rm = user.get(SessionManager.KEY_ID_RM);
		String id_user = user.get(SessionManager.KEY_ID);

		final ArrayList<Product> productlist = new ArrayList<Product>();


		JsonArrayRequest movieReq = new JsonArrayRequest(url_minuman,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								String  DInda = obj.getString(TAG_NAMA);
								String Harga = obj.getString(TAG_HARGA);
								String Image = obj.getString(TAG_IMAGE);
								String id = obj.getString(TAG_ID);
								String idKatagori = obj.getString(TAG_ID_KATAGORI);
								productlist.add(new Product(DInda,DInda,DInda,Harga,"10",Harga,"0",Image,id,idKatagori));

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});



		AppController.getInstance().addToRequestQueue(movieReq);
		productMap.put("List Menu Minuman", productlist);
		CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);





	}

	public void getNasikotak() {

		ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();
		SessionManager session;
		session = new SessionManager(AppController.getcontext());
		HashMap<String, String> user = session.getUserDetails();
		String id_rm = user.get(SessionManager.KEY_ID_RM);
		String id_user = user.get(SessionManager.KEY_ID);

		final ArrayList<Product> productlist = new ArrayList<Product>();


		JsonArrayRequest movieReq = new JsonArrayRequest(url_nasikotak,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								String  DInda = obj.getString(TAG_NAMA);
								String Harga = obj.getString(TAG_HARGA);
								String Image = obj.getString(TAG_IMAGE);
								String id = obj.getString(TAG_ID);
								String idKatagori = obj.getString(TAG_ID_KATAGORI);
								productlist.add(new Product(DInda,DInda,DInda,Harga,"10",Harga,"0",Image,id,idKatagori));

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());


			}
		});



		AppController.getInstance().addToRequestQueue(movieReq);
		productMap.put("List Menu Nasi Kotak", productlist);
		CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);





	}

	public static FakeWebServer getFakeWebServer() {

		if (null == fakeServer) {
			fakeServer = new FakeWebServer();
		}
		return fakeServer;
	}

	public void getAllProducts(int productCategory) {

		if (productCategory == 0) {

			getAllElectronic();
		} else if( productCategory  == 1){

			getAllFurnitures();

		}
		else
		{
			getNasikotak();
		}


	}

}
