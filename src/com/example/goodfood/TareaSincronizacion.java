package com.example.goodfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class TareaSincronizacion extends AsyncTask<Object, Void, Integer> {

	public static String ipServer="192.168.0.10";

	@Override
	protected Integer doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		System.out.println(ipServer);
		HttpClient cliente=new DefaultHttpClient();
	    HttpGet hget=new HttpGet("http://"+ipServer+"/Conexion.php?tabla=Nombre");
	    getValores(cliente, hget);
	    return 0;
	}
	
	private StringBuilder inputStreamToString(InputStream is) {
	    String rLine = "";
	    StringBuilder answer = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try {
	     while ((rLine = rd.readLine()) != null) {
	      answer.append(rLine);
	       }
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	     }
	    return answer;
	}
	
	private int getValores(HttpClient cliente, HttpGet httpget){
		int registros=0;
		try{
	    	HttpResponse response = cliente.execute(httpget);
	    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
	    	JSONArray mArray = new JSONArray(jsonResult);
	    	registros=mArray.length();
	    	for (int i = 0; i < registros; i++) {
	    	    JSONObject object = mArray.getJSONObject(i);
	    	    String id = object.getString("id");
	    	    String campo1 = object.getString("campo1");
	    	    String campo2 = object.getString("campo2");
	    	    System.out.println("id:"+id);
	    	}
	    }catch(JSONException e){
	    	e.printStackTrace();
	    } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return registros;
	}

}
