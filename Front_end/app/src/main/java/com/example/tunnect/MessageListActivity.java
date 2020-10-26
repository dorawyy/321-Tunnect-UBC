package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * The class for the activity that displays all of the chats that the user has.
 */
public class MessageListActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://35.236.81.110/time";
    private RequestQueue queue;
    RecyclerView chatOptions;
    ChatListAdaptor chatListAdaptor;
    RecyclerView.LayoutManager layoutManager;
    List<Chat> chatsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        queue = Volley.newRequestQueue(this);
        populateChatList();
        chatOptions = findViewById(R.id.chatOptions);
        chatOptions.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chatOptions.setLayoutManager(layoutManager);
        chatListAdaptor = new ChatListAdaptor(this, chatsList, chatOptions);
        chatOptions.setAdapter(chatListAdaptor);
    }

    // Using a Volley connection, this method adds entries in the chatsList from the provided server data
    private void populateChatList() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("chats");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject chat = jsonArray.getJSONObject(i);

                                chatsList.add(new Chat(chat.getLong("Id"), chat.getString("Name"),
                                        chat.getString("LastMessage"), chat.getString("Timestamp"), chat.getInt("Colour")));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed to retrieve data from server!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);

        // these entries are added for testing purposes
        //TODO: Delete this when testing is done!!!!!!!!!!!!!!!!!
        chatsList.add(new Chat(0, "David Onak", "That's sus man!", "10:33am", 0x44AA44));
        chatsList.add(new Chat(1, "Jeff", "My name is Jeff", "8:00am", 0x4444AA));
        chatsList.add(new Chat(2, "Nick Hamilton", "Your a beast!", "Oct. 24", 0xAA4444));
        chatsList.add(new Chat(3, "Joe Smith", "Hello, I am Linsay Lohan!", "Oct. 23", 0x222222));
    }

    // With the retrieved user id, opens a chat with that user
    public void openNewChat(long other_user_id) {
        Intent messageIntent = new Intent(MessageListActivity.this, MessagesActivity.class);
        startActivity(messageIntent);
    }
}