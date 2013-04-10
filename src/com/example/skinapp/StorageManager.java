
package com.example.skinapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class StorageManager {

    private String outBasePath;
    private AssetManager assetManager;
    private static final int BUFFER_SIZE = 1024;

    public StorageManager(Context context) {
        this(context, "html", false);
    }

    public StorageManager(Context context, String base, boolean useExternal) {
        this.assetManager = context.getResources().getAssets();
        this.outBasePath = useExternal ? Environment.getExternalStorageDirectory() + File.separator
                + context.getPackageName() :
                    context.getDir(base, Context.MODE_PRIVATE).getAbsolutePath();
    }

    public void copyFiles(String path) throws IOException {
        String[] fileList = assetManager.list(path);
        if (fileList == null || fileList.length == 0) {
            return;
        }
        String copyPath, outPath;
        File outFile;
        for (String file : fileList) {
            copyPath = file;
            if (!path.equals("")) {
                copyPath = path + File.separator + copyPath;
            }
            outPath = outBasePath + File.separator + copyPath;
            outFile = new File(outPath);
            if (isDirectory(copyPath)) {
                // ディレクトリ作成
                if (!outFile.exists()) {
                    outFile.mkdirs();
                }
                copyFiles(copyPath);
            } else {
                // ファイルコピー
                if (copyPath.toLowerCase().endsWith(".zip")) {
                    // zipファイルの解凍
                    unzip(copyPath, outFile.getParent());
                } else {
                    // コピー処理
                    copyData(copyPath, outFile);
                }
            }
        }
    }

    public void deleteAll(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteAll(f);
            }
        }
        file.delete();
    }

    private boolean isDirectory(final String path) throws IOException {
        boolean isDirectory = false;
        try {
            if (assetManager.list(path).length > 0) {
                isDirectory = true;
            } else {
                assetManager.open(path);
            }
        } catch (FileNotFoundException fnfe) {
            isDirectory = true;
        }
        return isDirectory;
    }

    private void unzip(String inPath, String outPath) throws IOException {
        ZipInputStream zis = null;
        InputStream is = null;
        FileOutputStream fos = null;
        File outDir = new File(outPath);
        try {
            is = assetManager.open(inPath, AssetManager.ACCESS_STREAMING);
            zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryFilePath = entry.getName().replace('\\', File.separatorChar);
                File outFile = new File(outDir, entryFilePath);
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    fos = new FileOutputStream(outFile);
                    writeData(zis, fos);
                    zis.closeEntry();
                }
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void copyData(String inPath, File outFile) throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        BufferedInputStream bis = null;
        try {
            is = assetManager.open(inPath);
            os = new FileOutputStream(outFile);
            bis = new BufferedInputStream(is);
            writeData(bis, os);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    private void writeData(InputStream is, OutputStream os) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(os);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

}
