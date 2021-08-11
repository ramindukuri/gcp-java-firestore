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
        storeData(db);
        readData(db);
    }

    public static void storeData(Firestore db) {
        DocumentReference docRef = db.collection("config").document("doc2");
        Map<String, Object> data = new HashMap<>();
        data.put("environment", "dev");
        data.put("project", "beam-pipeline1");
        data.put("key", "Lovelace");
        data.put("value", "1815");
        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void readData(Firestore db) {
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("config").whereEqualTo("environment","dev").whereEqualTo("project", "pipeline1").get();
        try {
            System.out.println(db.collection("config").document("doc1").get().get(30, TimeUnit.SECONDS).getData());
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("DocID: " + document.getId());
            System.out.println("Env: " + document.getString("environment"));
            System.out.println("project: " + document.getString("project"));
            System.out.println("key: " + document.getString("key"));
            System.out.println("value: " + document.getString("value"));

        }
    }

}

