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
import android.view.Menu;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class DetalleReceta extends Activity {

	private EditText textoIngredientes;
	private EditText textoInstrucciones;
	private EditText textoInfo;
	private TextView textoNombre;
	private TextView textoUsuario;
	private String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_receta);
		textoIngredientes=(EditText)this.findViewById(R.id.textoIngredientes);
		textoInstrucciones=(EditText)this.findViewById(R.id.textoInstrucciones);
		textoInfo=(EditText)this.findViewById(R.id.textoInfoNutricional);
		textoNombre=(TextView)this.findViewById(R.id.labelReceta);
		textoUsuario=(TextView)this.findViewById(R.id.textoUsuario);

		data=this.getIntent().getExtras().getString("data");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_receta, menu);
		return true;
	}

	private class TareaCargaInfo extends AsyncTask<String,Void,String[]>{
		
		private String respuesta[];
		private VisualizaReceta padre;
		
		public TareaCargaInfo(VisualizaReceta p){
			padre=p;
		}
		@Override
		protected String[] doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/GetInfoReceta.php?idUser="+arg0[0]+"&idReceta="+arg0[1];
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
		
		private String []getValores(HttpClient cliente, HttpGet httpget){
			String regs[]=null;
			try{
		    	HttpResponse response = cliente.execute(httpget);
		    	String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		    	JSONArray mArray = new JSONArray(jsonResult);
		    	int num_registros=mArray.length();
		    	for (int i = 0; i < num_registros; i++) {
		    		regs=new String[5];
		    	    JSONObject object = mArray.getJSONObject(i);
		    	    regs[0] = object.getString("ingredientes");
		    	    regs[1] = object.getString("pasos");
		    	    regs[2] = object.getString("info");
		    	    regs[3] = object.getString("detalle");
		    	    regs[4] = object.getString("user");
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
				textoUsuario.setText(respuesta[4]);
				textoNombre.setText(respuesta[3]);
				textoIngredientes.setText(respuesta[0]);
				textoInstrucciones.setText(respuesta[1]);
				textoInfo.setText(respuesta[2]);
			}
		}
	}
}
