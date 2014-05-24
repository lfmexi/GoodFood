package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Recetas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recetas);
		
		listaPublicadas=(ListView)this.findViewById(R.id.listaComentarios);
		listaFavoritas=(ListView)this.findViewById(R.id.listaFavoritas);
		
		listaPublicadas.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaPublicadasOnClick(arg1);
			}
		});
		
		listaFavoritas.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaFavoritasOnClick(arg1);
			}
		});
	}

	private void listaPublicadasOnClick(View arg1){
		//agregar la lógica para abrir la receta seleccionada
		this.startActivity(new Intent(this,VisualizaReceta.class));
	}
	
	private void listaFavoritasOnClick(View arg1){
		//agregar la lógica para abrir la receta seleccionada
		this.startActivity(new Intent(this,VisualizaReceta.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recetas, menu);
		return true;
	}
	
	private ListView listaPublicadas;
	private ListView listaFavoritas;
}
