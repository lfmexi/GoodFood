package com.example.goodfood;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Registro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		
		spinner=(Spinner)this.findViewById(R.id.spinnerSexo);
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

	private Spinner spinner;
}
