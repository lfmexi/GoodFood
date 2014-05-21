package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class BuscaReceta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busca_receta);
		Button buscar=(Button)this.findViewById(R.id.botonBuscarReceta);
		lista=(ListView)this.findViewById(R.id.listaResultados);
		
		buscar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buscarOnClick();
			}
		});
		
		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnClick(arg1);
			}
		});
	}

	private void buscarOnClick(){
		String listado[]=new String[]{
				"Ingresar nueva receta","Buscar recetas","Mis recetas","Ejercicios recomendados","Perfil"
		};
		lista.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listado));
	}
	
	private void listaOnClick(View v){
		this.startActivity(new Intent(this,VisualizaReceta.class));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busca_receta, menu);
		return true;
	}
	private ListView lista;
}
