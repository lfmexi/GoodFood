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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComentarioReceta extends Activity {

	private String cadenaData;
	private EditText texto_comment;
	private TextView nombre_receta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comentario_receta);
		cadenaData=this.getIntent().getExtras().getString("data");
		String [] datos=cadenaData.split(",");
		
		Button boton=(Button)this.findViewById(R.id.botonPublicarComentario);
		texto_comment=(EditText)this.findViewById(R.id.textoComentario);
		nombre_receta=(TextView)this.findViewById(R.id.labelNombreRecetaComentario);
		
		nombre_receta.setText(datos[2]);
		
		boton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonOnClick();
			}
		});
		
		
	}

	private void botonOnClick(){
		if(texto_comment.getText()!=null){
			String comentario=texto_comment.getText().toString();
			comentario=comentario.replace(" ", "%20");
			comentario=comentario.replace("\n","%0f");
			String [] datos=cadenaData.split(",");
			TaskComentario t=new TaskComentario(this);
			t.execute(datos[0],datos[1],comentario);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comentario_receta, menu);
		return true;
	}
	
	private class TaskComentario extends AsyncTask<String,Void,String>{
		
		private ComentarioReceta padre;
		private String respuesta;
		
		public TaskComentario(ComentarioReceta p){
			padre=p;
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
			String s="http://"+Login.ipServer+"/Comentario.php?idUser="+arg0[0]+"&idReceta="+arg0[1]+"&comentario="+arg0[2];
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
			if(respuesta!=null){
				padre.finish();
				//parent.startActivity(intent);
				//parent.finish();
			}
		}
		
	}

}
