package com.appodeal.vast;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class MediaFileLoader {
    private final String fileUrl;
    private final String cacheDir;

    MediaFileLoader(@NonNull String cacheDir, @NonNull String fileUrl) {
        this.fileUrl = fileUrl;
        this.cacheDir = cacheDir;
    }

    Uri getLocalFileUri() {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            File dir = new File(cacheDir);
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            int fileNameLength = 230 - dir.getPath().length();
            String tempName = "temp" + System.currentTimeMillis();
            String fileName = fileUrl.substring(0, Math.min(fileNameLength, fileUrl.length())).replace("/", "").replace(":", "");
            File cachedFile = new File(dir, fileName);
            if (cachedFile.exists()) {
                return Uri.fromFile(cachedFile);
            } else {
                File file = new File(dir, tempName);
                URL url = new URL(fileUrl);
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                FileOutputStream fileOutput = new FileOutputStream(file);
                long totalSize = connection.getContentLength();
                long downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                }
                fileOutput.close();
                if (totalSize == downloadedSize) {
                    //noinspection ResultOfMethodCallIgnored
                    file.renameTo(new File(dir, fileName));
                }
                return Uri.fromFile(new File(dir, fileName));
            }
        } catch (Exception e) {
            VastLog.e(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
        return null;
    }
}
