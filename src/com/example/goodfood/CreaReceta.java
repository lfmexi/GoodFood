package com.example.goodfood;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CreaReceta extends Activity {

	private String pathImagen="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crea_receta);
		
		Button boton=(Button)this.findViewById(R.id.botonSeleccionaImagen);
		
		imagen=(ImageView)this.findViewById(R.id.imageView1);
		
		boton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				seleccionarOnClick();
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
			Bitmap bm=BitmapFactory.decodeFile(pathImagen);
			imagen.setImageBitmap(bm);
		}
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	private ImageView imagen;
}
