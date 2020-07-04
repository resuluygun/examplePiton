package com.example.examplepiton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class DBOperations {


    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    static CollectionReference collectionReference = db.collection("tasks").document("userId").
            collection("task_list");

    static DocumentReference documentReference= collectionReference.document();


}
