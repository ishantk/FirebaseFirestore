package com.auribises.studentfeedback.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auribises.studentfeedback.R;
import com.auribises.studentfeedback.model.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParsingActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    StringRequest stringRequest;

    ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonparsing);

        bookList = new ArrayList<>();

        retrieveDataFromServer();
    }

    void retrieveDataFromServer(){

        requestQueue = Volley.newRequestQueue(this);
        String url = "http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2";

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPONSE",response);
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR",error.getMessage());
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest); // Will Execute Request
    }

    void parseResponse(String response){

        String price="", name="", author="";
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("bookstore");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jObj = jsonArray.getJSONObject(i);
                price = jObj.getString("price");
                name = jObj.getString("name");
                author = jObj.getString("author");

                Log.i("DATA",price+" "+name+" "+author);

                Book book = new Book(price,name,author);
                bookList.add(book);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
