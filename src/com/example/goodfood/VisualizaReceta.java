package com.example.goodfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VisualizaReceta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualiza_receta);
		nombreReceta=(TextView)this.findViewById(R.id.labelNombreReceta);
		barra=(RatingBar)this.findViewById(R.id.ratingBar1);
		botonComenta=(Button)this.findViewById(R.id.botonComentar);
		
		barra.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				barraOnRatingBarChange(arg1);
			}
		});
		
		botonComenta.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				botonComentaOnClick();
			}
		});
		
	}
	
	private void botonComentaOnClick(){
		this.startActivity(new Intent(this,ComentarioReceta.class));
	}

	private void barraOnRatingBarChange(float valorNuevo){
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualiza_receta, menu);
		return true;
	}

	private TextView nombreReceta;
	private RatingBar barra;
	private Button botonComenta;
}
