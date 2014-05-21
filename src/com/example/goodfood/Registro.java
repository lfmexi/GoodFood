package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Registro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		
		Button registrar=(Button)this.findViewById(R.id.buttonRegistrar);
		Button cancel=(Button)this.findViewById(R.id.buttonCancelar);
		
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
		Intent i=new Intent(this,CompletaPerfil.class);
		this.startActivity(i);
		this.finish();
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

}
