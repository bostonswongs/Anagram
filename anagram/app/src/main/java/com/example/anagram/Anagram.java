package com.example.anagram;

import android.content.Context;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import com.google.firebase.FirebaseApp;


import java.util.Random;

public class Anagram {
    private static boolean newGame = true;
    public static final Random RANDOM = new Random();
    public static String[][] WORDS;
    public static int tier = 1;

    private static void loadAllWords() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference easyRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Easy.csv");
        StorageReference medRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Medium.csv");
        StorageReference hardRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/anagram-208e5.appspot.com/o/Hard.csv");

        final long ONE_MEGABYTE = 1024 * 1024;
        tier = 7;

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Downloads");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"Easy.csv");

        easyRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });


        easyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Data for "images/island.jpg" is returns, use this as needed
                System.out.print(uri);
                tier = 55;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                System.out.print("FUCK easy");
                tier = 3;
            }
        });
        medRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                System.out.print(bytes);
                tier = 55;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.print("FUCK med");
                tier = 3;
            }
        });
        hardRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                System.out.print(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.print("FUCK hard");
            }
        });
    }

    public static String randomWord(int score) {
        if(score % 10 == 0) {
            if(score == 0) {
                tier = 0;
                if(newGame) {
                    loadAllWords();
                    newGame = false;
                }
            }
//            tier++;
        }
//        if(tier == 55) {
//            return String.valueOf(hardRef);
//        }
        if(tier == 3) {
            return "HARDY";
        }
        if(tier == 7) {
            return "YAYAY";
        }
        return "UDDERS";
    }

    public static String shuffleWord(String word) {
//        if (word != null  &&  !"".equals(word)) {
//            char a[] = word.toCharArray();
//
//            for (int i = 0; i < a.length; i++) {
//                int j = RANDOM.nextInt(a.length);
//                char tmp = a[i];
//                a[i] = a[j];
//                a[j] = tmp;
//            }
//
//            return new String(a);
//        }

        return word;
    }
}
