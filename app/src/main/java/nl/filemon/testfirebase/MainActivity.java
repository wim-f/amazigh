package nl.filemon.testfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
    Button button1, button2, button3, button4;
    TextView tvOutput;
    ImageView ivData;
    Spinner spWoorden;

    Integer aantal_woorden;
    //
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        tvOutput = findViewById(R.id.textView);
        ivData = findViewById(R.id.imgData);
        spWoorden = findViewById(R.id.spWoorden);

        button1.setText("Make Hello");
        button2.setText("Show Hello");
        button3.setText("List");
        button4.setText("Knop 4");

        //spDataArray.add("");
        aantal_woorden=0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                make_hello_world();
                break;
            case R.id.btn2:
                show_hello_world();
                break;
            case R.id.btn3:
                woorden_lijst(view);
                break;
            case R.id.btn4:

                break;
        }
    }

    private void make_hello_world() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://testfirebase-c03b4-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference myRef = database.getReference("message");

        Date currentTime = Calendar.getInstance().getTime();
        String m ="Hallo wereld! De tijd is: "+currentTime.toString();
        myRef.setValue(m);

        Toast.makeText(getApplicationContext(), "Melding: "+m,
                Toast.LENGTH_SHORT).show();
    }

    private void show_hello_world() {
        // reference aanmaken naar Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://testfirebase-c03b4-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("message");

        // maken eventlistener, deze wordt uitgevoerd bij Datachange
        ValueEventListener mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // waarde wordt gelezen uit de opgehaalde data
                String m = dataSnapshot.getValue().toString();

                // feedback aan gebruiker
                Toast.makeText(getApplicationContext(), "Opgeslagen: "+m,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        // eventlistener wordt gekoppeld aan de database reference
        myRef.addValueEventListener(mListener);
    }

    public void woorden_lijst(View view) {
        DatabaseReference databaseReference;
        ListView listView;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;

        setContentView(R.layout.activity_main);
        databaseReference=FirebaseDatabase.getInstance().getReference("woorden");
        //listView=(ListView)findViewById(R.id.listView1);
        arrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //listView.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value=snapshot.getValue(Woorden.class).toString();
                arrayList.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}