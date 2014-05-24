package com.example.goodfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Registro extends Activity {


	private Spinner spinner;
	private EditText campoUsername;
	private EditText campoCorreo;
	private EditText campoNombre;
	private EditText campoApellido;
	private EditText campoPass;
	private EditText campoConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		
		spinner=(Spinner)this.findViewById(R.id.spinnerSexo);
		campoUsername=(EditText)this.findViewById(R.id.textUserReg);
		campoCorreo=(EditText)this.findViewById(R.id.textCorreo);
		campoNombre=(EditText)this.findViewById(R.id.textNombre);
		campoApellido=(EditText)this.findViewById(R.id.textApellido);
		campoPass=(EditText)this.findViewById(R.id.textPassReg);
		campoConfirm=(EditText)this.findViewById(R.id.textConfirmPass);
		Button registrar=(Button)this.findViewById(R.id.buttonRegistrar);
		Button cancel=(Button)this.findViewById(R.id.buttonCancelar);
		
		List<String> list = new ArrayList<String>();
        list.add("Hombre");
        list.add("Mujer");
         
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
		
		registrar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registrar();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelar();
			}
		});
	}

	private void registrar(){
		if(this.campoApellido.getText()!=null && this.campoConfirm.getText()!=null && this.campoCorreo.getText()!=null
				&& this.campoNombre.getText()!=null && this.campoPass.getText()!=null &&this.campoUsername.getText()!=null){
			String apellido=campoApellido.getText().toString().replace(" ", "%20");
			String confirm=campoConfirm.getText().toString().replace(" ", "%20");
			String correo=campoCorreo.getText().toString().replace(" ", "%20");
			String nombre=campoNombre.getText().toString().replace(" ", "%20");
			String pass=campoPass.getText().toString().replace(" ", "%20");
			String user=campoUsername.getText().toString().replace(" ", "%20");
			
			if(confirm.equals(pass)){
				TareaRegistra tr=new TareaRegistra(this);
				tr.execute(user,correo,nombre,apellido,pass);
			}
		}
	}
	
	private void cancelar(){
		this.finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	private class TareaRegistra extends AsyncTask<String,Void,String>{
		
		private Registro parent;
		private String respuesta=null;
		
		public TareaRegistra(Registro par){
			parent=par;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
		    String s="http://"+Login.ipServer+"/Registro.php?user="+params[0]+"&email="+params[1]+"&nombre="+params[2]
		    		+"&apellido="+params[3]+"&pass="+params[4];
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
				Intent intent=new Intent(parent,CompletaPerfil.class);
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
