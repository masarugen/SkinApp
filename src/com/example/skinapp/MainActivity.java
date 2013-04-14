
package com.example.skinapp;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

//http://www.adakoda.com/adakoda/2012/02/android-45.html
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageButton1 = (ImageView) findViewById(R.id.imageButton1);

        // StorageManager storageManager = new
        // StorageManager(getApplicationContext());
        // try {
        // storageManager.copyFiles("");
        // } catch (IOException e) {
        // e.printStackTrace();
        // } finally {
        // String path = getDir("html", Context.MODE_PRIVATE).getAbsolutePath()
        // + File.separator
        // + "aaa" + File.separator + "test.png";
        // imageButton1.setImageDrawable(Drawable.createFromPath(path));
        // File file = new File(path);
        // Log.d("test",
        // "file:" + file.exists() + ":" + file.getAbsolutePath() + ":"
        // + file.isDirectory());
        // }
        AssetStorage as = new AssetStorage(getApplicationContext(), "html");
        try {
            as.copyFiles("", "out");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            String path = getDir("html", Context.MODE_PRIVATE).getAbsolutePath() + File.separator
                    + "out" + File.separator + "bbb" + File.separator
                    + "test.png";
            imageButton1.setImageDrawable(Drawable.createFromPath(path));
            File file = new File(path);
            Log.d("test",
                    "outfile:" + file.exists() + ":" + file.getAbsolutePath() + ":"
                            + file.isDirectory());
        }
    }
}
