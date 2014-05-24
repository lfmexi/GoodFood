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
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BuscaReceta extends Activity {


	private ListView lista;
	private String idUser;
	private EditText textoBusqueda;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busca_receta);
		Button buscar=(Button)this.findViewById(R.id.botonBuscarReceta);
		lista=(ListView)this.findViewById(R.id.listaResultados);
		textoBusqueda=(EditText)this.findViewById(R.id.textoComentario);
		
		idUser=this.getIntent().getExtras().getString("idUser");
		
		buscar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buscarOnClick();
			}
		});
		
		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnClick(arg1);
			}
		});
	}

	private void buscarOnClick(){
		if(textoBusqueda.getText()!=null){
			String patron=textoBusqueda.getText().toString();
			BuscaRecetaAT b=new BuscaRecetaAT(this);
			b.execute(patron);
		}
	}
	
	private void listaOnClick(View v){
		String nombreMenu=((TextView) v).getText().toString();
		if(nombreMenu!=null){
			SeleccionReceta sr=new SeleccionReceta(this);
			sr.execute(nombreMenu);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_receta, menu);
		return true;
	}
	
	private class SeleccionReceta extends AsyncTask<String,Void,String>{

		private BuscaReceta padre;
		private String respuesta;
		private String receta;
		public SeleccionReceta(BuscaReceta p){
			padre=p;
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			receta=params[0];
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/SeleccionaReceta.php?descrip="+params[0];
			HttpGet hget=new HttpGet(s);
		    respuesta =getValores(cliente, hget);
		    return respuesta;
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
		
		private String getValores(HttpClient cliente, HttpGet httpget){
			String regs=null;
			try{
		    	HttpResponse response = cliente.execute(httpget);
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	for (int i = 0; i < num_registros; i++) {
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    String campo1 = object.getString("id");
		    	    regs=campo1;
		    	}
		    }catch(JSONException e){
		    	e.printStackTrace();
		    } catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return regs;
		}
		
		protected void onPostExecute(String result){
			if(respuesta!=null){
				String cadena=idUser+","+respuesta+","+receta;
				Intent i=new Intent(padre,VisualizaReceta.class);
				i.putExtra("val", cadena);
				padre.startActivity(i);
				//lista.setAdapter(new ArrayAdapter<String>(padre,android.R.layout.simple_list_item_1,android.R.id.text1,respuesta));
			}else{
				
			}
		}
		
	}
	
	private class BuscaRecetaAT extends AsyncTask<String,Void,String[]>{
		
		private BuscaReceta padre;
		private String [] respuesta;
		
		public BuscaRecetaAT(BuscaReceta p){
			padre=p;
		}
		@Override
		protected String[] doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/BuscaReceta.php?patron="+arg0[0];
			HttpGet hget=new HttpGet(s);
		    respuesta =getValores(cliente, hget);
		    return respuesta;
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
		
		private String [] getValores(HttpClient cliente, HttpGet httpget){
			String []regs=null;
			try{
		    	HttpResponse response = cliente.execute(httpget);
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	regs = new String[num_registros];
		    	for (int i = 0; i < num_registros; i++) {
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    String campo1 = object.getString("descripcion");
		    	    regs[i]=campo1;
		    	}
		    }catch(JSONException e){
		    	e.printStackTrace();
		    } catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return regs;
		}
		
		protected void onPostExecute(String[] result){
			if(respuesta!=null){
				lista.setAdapter(new ArrayAdapter<String>(padre,android.R.layout.simple_list_item_1,android.R.id.text1,respuesta));
			}else{
				
			}
		}
		
	}
}
