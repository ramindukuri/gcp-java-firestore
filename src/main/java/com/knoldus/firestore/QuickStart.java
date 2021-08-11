package com.knoldus.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class QuickStart {
    public static void main(String[] args) {
        String projectId = "ultimate-dev-308018";
        FirestoreOptions firestoreOptions = null;
        {
            try {
                firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId(projectId)
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Firestore db = firestoreOptions.getService();
        String environment = "prod";
        String project = "pipeline1";

        String doc = environment+"-"+project ;
        storeData(db,doc);
        readData(db,doc);
    }

    public static void storeData(Firestore db,String doc) {

        DocumentReference docRef = db.collection("config").document(doc);
        Map<String, Object> data = new HashMap<>();
        data.put("loglevel", "info");
        data.put("bootstrapservres", "localhost:9092");
        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            System.out.println("Updated document " +doc +":" + " Time" + result.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void readData(Firestore db,String doc) {
        try {
            System.out.println(db.collection("config").document(doc).get().get(30, TimeUnit.SECONDS).getData());
        } catch (InterruptedException | TimeoutException |ExecutionException e) {
            e.printStackTrace();
        }
    }

}

