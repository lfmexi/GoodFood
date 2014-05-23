package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CompletaPerfil extends Activity {

	private String datos=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completa_perfil);
		Button actualiza=(Button)this.findViewById(R.id.botonActualizarPerfil);
		Button ahoraNo=(Button)this.findViewById(R.id.botonNotNow);
		
		Intent intent=this.getIntent();
		datos=intent.getExtras().getString("valueString");
		
		actualiza.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				actualizarOnClick();
			}
		});
		
		ahoraNo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ahoraNoOnClick();
			}
		});
	}

	private void actualizarOnClick(){
		//acciones para actualizar el perfil
		Intent i=new Intent(this,MenuPrincipal.class);
		i.putExtra("valueString", datos);
		this.startActivity(i);
		this.finish();
	}
	
	private void ahoraNoOnClick(){
		//solo se salta éste paso
		Intent i=new Intent(this,MenuPrincipal.class);
		i.putExtra("valueString", datos);
		this.startActivity(i);
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.completa_perfil, menu);
		return true;
	}

}
