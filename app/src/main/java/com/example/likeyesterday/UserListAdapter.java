package com.example.likeyesterday;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;
    Context context;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String currentUserName;
    String currentUserPhoneNumber;

    public UserListAdapter(Context context,ArrayList<UserObject> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_list_item, null, false);
        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListViewHolder holder, final int position) {
        holder.nameTextView.setText(userList.get(position).getName());
        holder.phoneTextView.setText(userList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    class UserListViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, phoneTextView;
        ConstraintLayout addUserConstraintLayout;
        UserListViewHolder(View view){
            super(view);
            nameTextView = view.findViewById(R.id.nameTextViewAddFriendsList);
            phoneTextView = view.findViewById(R.id.phoneNumberTextViewAddFriendsList);
            addUserConstraintLayout = view.findViewById(R.id.addUserConstraintLayout);

            addUserConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CollectionReference friendListReference = db.collection("Users").document(uid).collection("FriendsList");
                    friendListReference
                            .whereEqualTo("PhoneNumber",phoneTextView.getText().toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    Log.i("friendTest", "no of users: "+queryDocumentSnapshots.size());

                                    if(queryDocumentSnapshots.size()!=0){
                                        Toast.makeText(context,"User is already in your friend list",Toast.LENGTH_SHORT).show();
                                        Log.d("friend","User Already in your friend list");
                                    }
                                    if(queryDocumentSnapshots.size()==0 ){
                                        CollectionReference userCollectionReference = db.collection("Users");
                                        userCollectionReference
                                                .whereEqualTo("Phone Number",phoneTextView.getText().toString())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                                            String friendDocID=documentSnapshot.getId();
                                                            String friendFullName=documentSnapshot.get("Full Name").toString();
                                                            String friendPhoneNumber=documentSnapshot.get("Phone Number").toString();

                                                            Map<String,Object> friend = new HashMap<>();
                                                            friend.put("FullName",friendFullName);
                                                            friend.put("PhoneNumber",friendPhoneNumber);

                                                            friendListReference.document(friendDocID).set(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    DocumentReference currentUserDocumentReference = db.collection("Users").document(uid);

                                                                    currentUserDocumentReference.get()
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    if(documentSnapshot.exists()){

                                                                                        currentUserName=documentSnapshot.get("Full Name").toString();
                                                                                        currentUserPhoneNumber=documentSnapshot.get("Phone Number").toString();
                                                                                        int noOfFriends=Integer.parseInt(documentSnapshot.get("Number of friends").toString());
                                                                                        currentUserDocumentReference.update("Number of friends",noOfFriends+1 );
                                                                                    }

                                                                                    DocumentReference friendDocumentReference = db.collection("Users").document(friendDocID);
                                                                                    friendDocumentReference.get()
                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                    if(documentSnapshot.exists()){
                                                                                                        int noOfRequests=Integer.parseInt(documentSnapshot.get("Number of requests").toString());
                                                                                                        friendDocumentReference.update("Number of requests",noOfRequests+1 );

                                                                                                        CollectionReference requestListReference = friendDocumentReference.collection("Request List");

                                                                                                        Map<String,Object> request = new HashMap<>();
                                                                                                        request.put("FullName",currentUserName);
                                                                                                        request.put("PhoneNumber",currentUserPhoneNumber);

                                                                                                        requestListReference.document(uid).set(request);
                                                                                                        Toast.makeText(context,"User added to your friend list",Toast.LENGTH_SHORT).show();
                                                                                                        notifyDataSetChanged();
                                                                                                        notifyItemRemoved(getAdapterPosition());
                                                                                                    }
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(context,"couldn't send request",Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                                                    Log.d("Add friend",e.toString());
                                                                                }
                                                                            });

                                                                }
                                                            })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                                            Log.d("Add friend",e.toString());

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                        Log.d("Add friend",e.toString());
                                                    }
                                                });
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("friend",e.toString());
                                }
                            });





                }
            });

        }
    }
}
