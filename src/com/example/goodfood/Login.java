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
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	
	public static String ipServer="192.168.0.10";
	
	private EditText textoUser;
	private EditText textoPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		textoUser=(EditText)this.findViewById(R.id.textUsername);
		textoPass=(EditText)this.findViewById(R.id.textPasswd);
		Button registrar=(Button)this.findViewById(R.id.botonRegistro);
		Button recuperar=(Button)this.findViewById(R.id.botonRec);
		Button login=(Button)this.findViewById(R.id.botonLogin);
		
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loginOnClick();
			}
		});
		
		registrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RegistrarOnClick();
			}
		});
		
		recuperar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				recuperarOnClick();
			}
		});
	}

	private void loginOnClick(){
		//Acciones de validación del usuario
		if(textoUser.getText()!=null && textoPass.getText()!=null){
			String nombre=textoUser.getText().toString();
			String pass=textoPass.getText().toString();
			
			if(!pass.equals(null)&&!nombre.equals(null)){
				TareaAsincrona ws=new TareaAsincrona(this);
				ws.execute(nombre,pass);
			}
		}
	}
	private void RegistrarOnClick(){
		Intent i=new Intent(this,Registro.class);
		this.startActivity(i);
	}
	
	private void recuperarOnClick(){
		Intent i=new Intent(this,Recupera.class);
		this.startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private class TareaAsincrona extends AsyncTask<String,Void,String>{
		
		private Login parent;
		private String respuesta=null;
		
		public TareaAsincrona(Login par){
			parent=par;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println(ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+ipServer+"/Conexion1.php?user="+params[0]+"&pass="+params[1];
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
		    	    String id = object.getString("id");
		    	    String campo1 = object.getString("nick");
		    	    String campo2 = object.getString("nombre");
		    	    regs=id+","+campo1+","+campo2;
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
				Intent intent=new Intent(parent,MenuPrincipal.class);
				intent.putExtra("valueString", respuesta);
				parent.startActivity(intent);
				parent.finish();
			}else{
				AlertDialog.Builder alert=new AlertDialog.Builder(parent);
				alert.setTitle("Error de autenticación");
				alert.setMessage("Usuario o contraseña no válidos");
				alert.show();
			}
		}
	}
}
