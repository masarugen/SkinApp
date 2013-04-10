
package com.example.skinapp;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.AsyncTask;

public class StrageAsyncTask extends AsyncTask<Void, Void, Void> {
    @SuppressWarnings("unused")
    private static final String TAG = "StrageAsyncTask";
    private final StrageAsyncTask self = this;

    private WeakReference<Context> contextRef;
    private String base;

    public StrageAsyncTask(Context context, String base) {
        this.contextRef = new WeakReference<Context>(context);
        this.base = base;
    }

    /**
     * 前処理
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * バックグラウンド処理
     */
    @Override
    protected Void doInBackground(Void... params) {
        if (contextRef != null) {
            StorageManager storageManager = new StorageManager((Context) contextRef.get());
            try {
                storageManager.copyFiles("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 実行後処理
     */
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    /**
     * キャンセル処理
     */
    @Override
    protected void onCancelled(Void result) {
        super.onCancelled();
    }
}
