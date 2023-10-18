package org.RodriDev.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonJoinChat;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonJoinChat = findViewById(R.id.buttonJoinChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);

        chatAdapter = new ChatAdapter();
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("chat");

        // Recuperar mensajes existentes de Firebase Realtime Database al iniciar la aplicación
        DatabaseReference messagesRef = databaseReference.child("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String usuari = snapshot.child("usuari").getValue(String.class);
                    String message = snapshot.child("message").getValue(String.class);
                    chatAdapter.addMessage(usuari, message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!name.isEmpty()) {
                    currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        joinChat(name);
                        Toast.makeText(MainActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void joinChat(String name) {
        DatabaseReference userRef = databaseReference.child("users").child(currentUser.getUid());
        userRef.child("name").setValue(name);
    }

    private void sendMessage(String message) {
        String senderName = editTextName.getText().toString().trim();
        if (!senderName.isEmpty()) {
            DatabaseReference messagesRef = databaseReference.child("messages");
            DatabaseReference newMessageRef = messagesRef.push();
            newMessageRef.child("usuari").setValue(senderName);
            newMessageRef.child("message").setValue(message);
        }
    }
}

