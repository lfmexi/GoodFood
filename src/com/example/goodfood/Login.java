package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button registrar=(Button)this.findViewById(R.id.botonRegistro);
		Button recuperar=(Button)this.findViewById(R.id.botonRec);
		
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

}
