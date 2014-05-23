package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuPrincipal extends Activity {

	private String cadena=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
		String listado[]=new String[]{
				"Ingresar nueva receta","Buscar recetas","Mis recetas","Ejercicios recomendados","Perfil"
		};
		
		Intent i=this.getIntent();
		cadena=i.getExtras().getString("valueString");
		
		lista=(ListView)this.findViewById(R.id.listaResultados);
		lista.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,listado));
		
		lista.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				listaOnClick(arg1);
			}
		});
	}

	private void listaOnClick(View view){
		String nombreMenu=((TextView) view).getText().toString();
		if(nombreMenu.equals("Ingresar nueva receta")){
			String[]info=cadena.split(",");
			Intent i=new Intent(this,CreaReceta.class);
			i.putExtra("idUser", info[0]);
			this.startActivity(i);
		}else if(nombreMenu.equals("Buscar recetas")){
			this.startActivity(new Intent(this,BuscaReceta.class));
		}else if(nombreMenu.equals("Mis recetas")){
			this.startActivity(new Intent(this,Recetas.class));
		}else if(nombreMenu.equals("Ejercicios recomendados")){
			this.startActivity(new Intent(this,Ejercicios.class));
		}else if(nombreMenu.equals("Perfil")){
			this.startActivity(new Intent(this,Perfil.class));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

	private ListView lista;
}
