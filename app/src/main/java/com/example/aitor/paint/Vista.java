package com.example.aitor.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by aitor on 02/02/2015.
 */
public class Vista extends View{
    private int alto,ancho;
    private float x0=-1,y0=-1,xi=-1,yi=-1;
    private Paint pincel;
    private List<Recta> rectas=new ArrayList<Recta>();
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private double radio=0;
    private Path rectaPoligonal = new Path();
    private String figura="libre";
    public boolean relleno=false;
    private int grosor=2,color=Color.BLACK;

    class Recta{
        public float x0,y0,xi,yi;


    }
    public Vista(Context context) {
        super(context);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        alto=h;
        ancho=w;
        mapaDeBits = Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
        mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo = new Canvas(mapaDeBits);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mapaDeBits, 0, 0, null);

            pincel = new Paint();
        if(figura.compareTo("goma")!=0) {
            pincel.setColor(this.color);
            pincel.setAntiAlias(true);
            pincel.setStrokeWidth(grosor);
            if(!relleno) {
                pincel.setStyle(Paint.Style.STROKE);
            }else{
                pincel.setStyle(Paint.Style.FILL);
            }
        }else{
            pincel.setColor(Color.WHITE);
            pincel.setStrokeWidth(grosor);
            pincel.setAntiAlias(true);
            pincel.setStyle(Paint.Style.STROKE);
        }
        switch (figura){
            case "recta":
                for(Recta r: rectas)
                canvas.drawLine(r.x0, r.y0, r.xi, r.yi, pincel);
                canvas.drawBitmap(mapaDeBits, 0, 0, null);
                canvas.drawLine(x0, y0, xi, yi, pincel);
                break;
            case "libre":
                canvas.drawBitmap(mapaDeBits, 0, 0, null);
                canvas.drawPath(rectaPoligonal, pincel);
                break;
            case "cuadrado":
                canvas.drawBitmap(mapaDeBits, 0, 0, null);
                float xorigen = Math.min(x0, xi);
                float xdestino = Math.max(x0, xi);
                float yorigen = Math.min(y0, yi);
                float ydestino = Math.max(y0, yi);
                canvas.drawRect(xorigen, yorigen, xdestino, ydestino, pincel);
            break;
            case "circulo":

                canvas.drawBitmap(mapaDeBits, 0, 0, null);
                canvas.drawCircle(x0, y0, (float)radio, pincel);

            break;
            case "goma":
                canvas.drawBitmap(mapaDeBits, 0, 0, null);
                canvas.drawPath(rectaPoligonal, pincel);
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                switch (figura) {
                    case "recta":
                        x0 = x;
                        y0 = y;
                    break;
                    case "libre":
                        x0 = x;
                        y0 = y;
                        rectaPoligonal.reset();
                        rectaPoligonal.moveTo(x0, y0);
                    break;
                    case "cuadrado":
                        x0 = x;
                        y0 = y;
                    break;
                    case "circulo":
                        x0 = x;
                        y0 = y;
                    break;
                    case "goma":
                        x0 = x;
                        y0 = y;
                        rectaPoligonal.reset();
                        rectaPoligonal.moveTo(x0, y0);
                    break;

                }
                break;
            case MotionEvent.ACTION_MOVE:

                switch (figura){
                    case "recta":
                        xi = x;
                        yi = y;
                        invalidate();
                    break;
                    case "libre":
                        xi = x;
                        yi = y;
                        rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi)/2);
                        invalidate();
                    break;
                    case "cuadrado":
                        xi = x;
                        yi = y;
                        invalidate();
                    break;
                    case "circulo":
                        xi = x;
                        yi = y;
                        radio = Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                        invalidate();
                    break;
                    case "goma":
                        xi = x;
                        yi = y;
                        rectaPoligonal.quadTo(xi, yi, (x + xi) / 2, (y + yi)/2);
                        invalidate();
                    break;

                }

                break;
            case MotionEvent.ACTION_UP:
                switch (figura){
                    case "recta":
                        xi = x;
                        yi = y;
                        lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                        invalidate();
                        x0=y0=xi=yi=-1;
                    break;
                    case "libre":
                        xi = x;
                        yi = y;
                        lienzoFondo.drawPath(rectaPoligonal, pincel);
                        invalidate();
                        x0=y0=xi=yi=-1;
                    break;
                    case "cuadrado":
                        xi = x;
                        yi = y;
                        if(xi < x0 || yi < y0)
                            lienzoFondo.drawRect(xi, yi, x0, y0, pincel);
                        else
                            lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                    break;
                    case "circulo":
                        xi = x;
                        yi = y;
                        radio = Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                        lienzoFondo.drawCircle(x0, y0, (float)radio, pincel);
                        radio = 0;
                        x0=y0=xi=yi=-1;
                    break;
                    case "goma":
                        xi = x;
                        yi = y;
                        lienzoFondo.drawPath(rectaPoligonal, pincel);
                        invalidate();
                        x0=y0=xi=yi=-1;
                    break;

                }
                break;
        }
        return true;
    }



public void mostrarHerramientas(){
    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
    LayoutInflater inflater=LayoutInflater.from(getContext());
    final View view=inflater.inflate(R.layout.activity_dialogo__figuras,null);
    alert.setMessage("HERRAMIENTAS");
    alert.setView(view);
    //alert.setCancelable(false);
    final CheckBox checkRelleno=(CheckBox)view.findViewById(R.id.checkrelleno);
    final RadioGroup grupo=(RadioGroup)view.findViewById(R.id.grupo);
    checkRelleno.setChecked(relleno);
    if(figura=="libre"){
        grupo.check(R.id.btnlibre);
    }else if(figura=="cuadrado"){
        grupo.check(R.id.btnrectangulo);
    }if(figura=="circulo"){
        grupo.check(R.id.btncirculo);
    }if(figura=="recta"){
        grupo.check(R.id.btnrecta);
    }if(figura=="goma"){
        grupo.check(R.id.btngoma);
    }
    SeekBar barraGrosor=(SeekBar)view.findViewById(R.id.seekBar);
    barraGrosor.setProgress(grosor);
    barraGrosor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            grosor=progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });


    alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(checkRelleno.isChecked()){
                relleno=true;
            }else{
                relleno=false;
            }
           if(grupo.getCheckedRadioButtonId()==R.id.btnrecta){
               figura="recta";
           }else if(grupo.getCheckedRadioButtonId()==R.id.btnlibre){
               figura="libre";
           }else if(grupo.getCheckedRadioButtonId()==R.id.btncirculo){
               figura="circulo";
           }else if(grupo.getCheckedRadioButtonId()==R.id.btnrectangulo){
               figura="cuadrado";
           }else if(grupo.getCheckedRadioButtonId()==R.id.btngoma){
               figura="goma";
           }
        }
    });
    alert.setNegativeButton("Cancelar",null);
    alert.show();
}


public void setColor(int color) {
        this.color=color;
}
public int getColor() {
        return color;
}

public void guardar(){
    AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
    LayoutInflater inflater=LayoutInflater.from(getContext());
    final View vista=inflater.inflate(R.layout.guardar,null);
    alert.setMessage("GUARDAR");
    alert.setView(vista);
    alert.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            EditText nombre=(EditText)vista.findViewById(R.id.etguardar);
            FileOutputStream salida;
            try {
                salida = new FileOutputStream(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+
                                "/"+nombre.getText()+".jpg");
                mapaDeBits.compress(Bitmap.CompressFormat.JPEG, 90, salida);
            } catch (FileNotFoundException e) {
            }
        }

    });
    alert.setNegativeButton("Cancelar",null);
    alert.show();
}



}
