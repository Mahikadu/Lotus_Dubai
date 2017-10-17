package com.prod.sudesi.lotusherbalsnew.dbConfig;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 27-10-2016.
 */

public class DatabaseCopy {

    Context context;

    String DB_file_path = "/data/data/com.prod.sudesi.lotusherbalsnew/databases/LOTUS.sqlite";
    String DB_name = "LOTUS.sqlite";


    public boolean copy(AssetManager am, Context mContext) {
        this.context = mContext;
        DB_file_path = "/data/data/" + context.getPackageName()
                + "/databases/LOTUS.sqlite";
        boolean isCopied = false;
        InputStream in = null;
        OutputStream out = null;
        System.out.println("callllllllllllll");
        try {
            // Database from asset folder
            in = am.open(DB_name);
            // Database copied into application folder

            if (new File(DB_file_path).exists()) {

                isCopied = true;
            } else {
                // what to do if it doesn't exist
                try {
                    File f = new File(DB_file_path);
                    f.getParentFile().mkdirs();
                    out = new FileOutputStream(f);

                    copyFile(in, out);
                    System.out.println("DB created");

                    isCopied = true;

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("error 1:" + e);
                }

            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            // secondDbCopy(am);
        } catch (Exception e) {

            System.out.println("error: " + e);
            isCopied = false;
        }

        return isCopied;

    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer);
        }
        Log.d("Sq1", "Success SQL");
    }

}
