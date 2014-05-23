package com.example.goodfood;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreaReceta extends Activity {

	private String pathImagen="";
	private ImageView imagen;
	private EditText textoNombre;
	private EditText textoIngredientes;
	private EditText textoInstrucciones;
	private EditText textoInfo;
	private String idUser;
	Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crea_receta);
		Intent i=this.getIntent();
		idUser=i.getExtras().getString("idUser");
		
		textoNombre=(EditText)this.findViewById(R.id.textNombreReceta);
		textoIngredientes=(EditText)this.findViewById(R.id.textIngredientes);
		textoInstrucciones=(EditText)this.findViewById(R.id.textInstruccionesReceta);
		textoInfo=(EditText)this.findViewById(R.id.textInfoNutricional);
		Button boton=(Button)this.findViewById(R.id.botonSeleccionaImagen);
		Button boton2=(Button)this.findViewById(R.id.botonPostReceta);
		
		imagen=(ImageView)this.findViewById(R.id.imagenUsuario);
		
		boton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				seleccionarOnClick();
			}
		});
		
		boton2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boton2OnClick();
			}
		});
		
		imagen.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				seleccionarOnClick();
			}
		});
	}
	
	private void boton2OnClick(){
		if(this.textoNombre.getText()!=null && this.textoIngredientes.getText()!=null && this.textoInstrucciones.getText()!=null
				&& this.textoInfo.getText()!=null && this.idUser!=null){
			String nombre=textoNombre.getText().toString();
			String ingredientes=textoIngredientes.getText().toString();
			String instrucciones=textoInstrucciones.getText().toString();
			String info=textoInfo.getText().toString();
			RegistraReceta tr=new RegistraReceta(this);
			tr.execute(idUser,nombre,ingredientes,instrucciones,info);
			
		}
	}
	
	private void seleccionarOnClick(){
		Intent intent=new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		this.startActivityForResult(Intent.createChooser(intent, "Completar seleccionando"), 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==1 && resultCode==Activity.RESULT_OK){
			Uri selectImageUri=data.getData();
			pathImagen=getPath(selectImageUri);
			bitmap=BitmapFactory.decodeFile(pathImagen);
			imagen.setImageBitmap(bitmap);
		}
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	private class RegistraReceta extends AsyncTask<String,Void,String>{

		private CreaReceta padre;
		private ProgressDialog dialog = new ProgressDialog(CreaReceta.this);
		private String respuesta=null;
		public RegistraReceta(CreaReceta p){
			padre=p;
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			System.out.println(Login.ipServer);
			HttpClient cliente=new DefaultHttpClient();
			String s="http://"+Login.ipServer+"/RegistroReceta.php?idUser="+arg0[0]+"&nombre="+arg0[1]+"&ingredientes="+arg0[2]
					+"&instrucciones="+arg0[3]+"&info="+arg0[4];
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
				Intent intent=new Intent(padre,CompletaPerfil.class);
				intent.putExtra("valueString", respuesta);
				//parent.startActivity(intent);
				//parent.finish();
			}
		}
	}
}
