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
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VisualizaReceta extends Activity {

	private String cadenaData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_receta);
		nombreReceta=(TextView)this.findViewById(R.id.labelNombreReceta);
		barra=(RatingBar)this.findViewById(R.id.ratingBar1);
		botonComenta=(Button)this.findViewById(R.id.botonComentar);
		botonAgrega=(Button)this.findViewById(R.id.botonAgregarReceta);
		botonMostrar=(Button)this.findViewById(R.id.botonMostrarDescrpicion);
		lista=(ListView)this.findViewById(R.id.listaComentarios);
		
		cadenaData=this.getIntent().getExtras().getString("val");
		String[]datos=cadenaData.split(",");
		
		nombreReceta.setText(datos[2]);
		
		barra.setRating(ratingInicial);
		
		
		botonComenta.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonComentaOnClick();
			}
		});
		
		botonMostrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonMostrarOnClick();
			}
		});
		
		botonAgrega.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonAgregaOnClick();
			}
		});
		
		
		if(datos[1]!=null){
			TareaCargaComentarios t=new TareaCargaComentarios(this);
			t.execute(datos[1]);
			
			TareaCargaPuntuacion t2=new TareaCargaPuntuacion(this);
			t2.execute(datos[0],datos[1]);
		}
	}
	
	private void botonAgregaOnClick(){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //agregar la receta a la cuenta del usuario
		        	agregar();
		        	break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Desea agregar ésta receta a sus recetas favoritas?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
	}
	
	private void agregar(){
		String datos[]=this.cadenaData.split(",");
		TareaAgregaFavoritos t=new TareaAgregaFavoritos(this);
		t.execute(datos[0],datos[1]);
		
	}
	
	private void botonMostrarOnClick(){
		Intent i=new Intent(this,DetalleReceta.class);
		i.putExtra("data", this.cadenaData);
		this.startActivity(i);
	}
	
	private void botonComentaOnClick(){
		Intent i=new Intent(this,ComentarioReceta.class);
		i.putExtra("data", cadenaData);
		this.startActivity(i);
	}

	private void rate(float val){
		//se realiza la accion de rate
		ratingInicial=val;
		TareaPuntuar t=new TareaPuntuar(this);
		String datos[]=this.cadenaData.split(",");
		t.execute(datos[0],datos[1],val+"");
	}
	
	private void barraOnRatingBarChange(final float valorNuevo){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            rate(valorNuevo);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            barra.setRating(ratingInicial);
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Desea calificar la aplicación?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualiza_receta, menu);
		return true;
	}

	private TextView nombreReceta;
	private RatingBar barra;
	private Button botonComenta;
	private Button botonAgrega;
	private Button botonMostrar;
	private float ratingInicial=0;
	private ListView lista;
	
	
	private class TareaAgregaFavoritos extends AsyncTask<String,Void,String>{
		private String respuesta;
		private VisualizaReceta padre;
		
		public TareaAgregaFavoritos(VisualizaReceta p){
			padre=p;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/AgregaFavoritos.php?idUser="+arg0[0]+"&idReceta="+arg0[1];
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
		    	if(num_registros>0){
		    		regs="ok";
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
			if(respuesta==null){
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Error");
				alert.setMessage("La receta ya es favorita");
				alert.show();
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Success");
				alert.setMessage("Receta agregada a favoritas");
				alert.show();
			}
		}
		
	}
	
	private class TareaCargaComentarios extends AsyncTask<String,Void,String[]>{
		
		private String[] respuesta;
		private VisualizaReceta padre;
		
		public TareaCargaComentarios(VisualizaReceta p){
			padre=p;
		}
		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/ComentariosReceta.php?idReceta="+params[0];
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
		    	    String campo1 = object.getString("user");
		    	    String campo2= object.getString("comentario");
		    	    regs[i]=campo1+"\n"+campo2;
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
	
	private class TareaCargaPuntuacion extends AsyncTask<String,Void,String>{
		
		private String respuesta;
		private VisualizaReceta padre;
		
		public TareaCargaPuntuacion(VisualizaReceta p){
			padre=p;
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/GetPuntuacion.php?idUser="+arg0[0]+"&idReceta="+arg0[1];
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
		    	    String campo1 = object.getString("valor");
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
				float f=Float.parseFloat(respuesta);
				barra.setRating(f);
			}
			barra.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
				@Override
				public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
					// TODO Auto-generated method stub
					barraOnRatingBarChange(arg1);
				}
			});
		}
	}
	
	private class TareaPuntuar extends AsyncTask<String,Void,String>{

		private String respuesta;
		private VisualizaReceta padre;
		
		public TareaPuntuar(VisualizaReceta p){
			padre=p;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/Puntuar.php?idUser="+arg0[0]+"&idReceta="+arg0[1]+"&valor="+arg0[2];
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
		    	if(num_registros>0){
		    		regs="ok";
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
			if(respuesta==null){
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Error");
				alert.setMessage("La receta ya ha sido valorada");
				alert.show();
				barra.setRating(ratingInicial);
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(padre);
				alert.setTitle("Success");
				alert.setMessage("Receta valorada");
				alert.show();
			}
		}
		
	}

}
