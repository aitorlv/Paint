package com.example.aitor.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;


public class Principal extends  Activity{
    private static final int PICK=0;
    private Vista v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v=new Vista(this);
        setContentView(v);
        //this.activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_herramientas) {
            heramientas();
            return true;
        }else if(id==R.id.action_colores){
            colores();
        }else if(id==R.id.action_guardar){
            v.guardar();
        }
        return super.onOptionsItemSelected(item);
    }



    public void heramientas(){

        v.mostrarHerramientas();
    }

    public void colores(){
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this,v.getColor(), new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                v.setColor(color);
            }

        });
        colorPickerDialog.show();
    }


}
