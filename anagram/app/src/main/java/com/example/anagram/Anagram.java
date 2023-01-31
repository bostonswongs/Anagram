package com.example.anagram;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileReader;
import com.google.firebase.FirebaseApp;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Anagram {
    public static final Random RANDOM = new Random();
    public static String[][] WORDS = new String[3][2000];
    public static int tier = 0;

    private static int[] WORDS_LEN = {1, 1, 1};

    private static void parseUriCsv(int level, Context context, String file, Uri uri) {
        final DownloadManager[] manager = new DownloadManager[1];
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + file);
        if(!f.exists()) {
            manager[0] = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);
            long reference = manager[0].enqueue(request);
        }
        try {
            if (!f.exists()) {
                Log.e("Parse", "File doesn't exist");
            }
            FileInputStream fis = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i = 0;
            while ((strLine = br.readLine()) != null) {
                if(!strLine.isEmpty()) {
                    WORDS[level][i] = strLine.toUpperCase();
                    i++;
                }
            }
            Log.e("Parse", level + " File done");
            WORDS_LEN[level] = i + 1;
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAllWords(Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference easyRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Easy.csv");
        StorageReference medRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Medium.csv");
        StorageReference hardRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Hard.csv");

        WORDS[0][0] = "HELLO";
        Log.e("firebase ","Trying the downloads");
        easyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("firebase ","uri created ?" +uri.toString());
                parseUriCsv(0, context, "/anagram/easy.csv", uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.e("firebase ",";local tem file not created " +exception.toString());
                WORDS[0][0] = "FAIL";
            }
        });
        medRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("firebase ","uri created ?" +uri.toString());
                parseUriCsv(1, context, "/anagram/medium.csv", uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.e("firebase ",";local tem file not created " +exception.toString());
                WORDS[1][0] = "FAIL";
            }
        });
        hardRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("firebase ","uri created ?" +uri.toString());
                parseUriCsv(2, context, "/anagram/hard.csv", uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.e("firebase ",";local tem file not created " +exception.toString());
                WORDS[2][0] = "FAIL";
            }
        });
    }

    public static String randomWord(int score) {
        if(score % 10 == 0) {
            if(tier != 3) {
                tier++;
            }
            if(score == 0) {
                tier = 0;
            }
        }
        if(tier == 3 && score > 35) {
            int randIndex = RANDOM.nextInt(3);
            return WORDS[randIndex][RANDOM.nextInt(WORDS_LEN[randIndex])];
        }
        return WORDS[tier][RANDOM.nextInt(WORDS_LEN[tier])];
    }

    public static String shuffleWord(String word) {
        if (word != null  &&  !"".equals(word)) {
            char a[] = word.toCharArray();

            for (int i = 0; i < a.length; i++) {
                int j = RANDOM.nextInt(a.length);
                char tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }

            return new String(a);
        }

        return word;
    }
}
