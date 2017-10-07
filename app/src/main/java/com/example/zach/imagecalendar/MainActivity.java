package com.example.zach.imagecalendar;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;



public class MainActivity extends Activity {

    Button button;
    ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.window);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", getFile());
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }


    private File getFile() {

        File folder = new File(Environment.getExternalStorageDirectory(),"image_calendar");
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/image_calendar/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }
}



package ImageToText;

import java.io.File;
import java.util.Scanner;
import net.sourceforge.tess4j.*;

public class TextIO {
    
    private String picture;
    private int size;
    private int capacity;
    private String[] dates;
    
    public TextIO (String image) {
        picture = image;
        capacity = 100; 
        size = 0;
        dates =  new String[capacity];
    }
    
    public String textFromImage(String imageName) {
        String newPic = imageName;
        String result = null;
        File imageFile = new File(newPic);
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        try {
            result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        
        return result;
    }
    public String[] textToArray() {
        
        Scanner myScanner = new Scanner(textFromImage(picture));
        int i = 0;
        while (myScanner.hasNext()) {
            if (i < capacity) {
                dates[i] = myScanner.next();
                size++;
                i++;
            }
            else {
                expandCapacity();
                dates[i] = myScanner.next();
                size++;
                i++;                
            }
        }
        return dates;            
        
    }
    private void expandCapacity() {

        @SuppressWarnings("unchecked")
        String[] newArray = (String[])new Object[this.capacity * 2];

        for (int i = 0; i < this.capacity; i++) {
            newArray[i] = this.dates[i];
        }

        this.dates = newArray;
        this.capacity *= 2;
    }
    
    public String[] getInfo(String[] array) {
        String[] finalArray = new String[3];
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("Date") || array[i].equals("When")) {
                finalArray[1] = array[i+1];                
            }
            if (array[i].equals("Location") || array[i].equals("Where ")) {
                finalArray[2] = array[i+1];
            }
            
        }
        return finalArray;
    }
    
            
    
    
    
    

}


