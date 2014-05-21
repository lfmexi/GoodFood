package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Recupera extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recupera);
		Button rec=(Button)this.findViewById(R.id.botonRecuperacion);
		
		rec.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recuperar();
			}
		});
	}
	
	private void recuperar(){
		//accionar lo de enviar al correo la contraseña nueva
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recupera, menu);
		return true;
	}

}
