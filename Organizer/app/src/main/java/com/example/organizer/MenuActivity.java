package com.example.organizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logOutId) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        if (item.getItemId() == R.id.deleteUserId) {
            final EditText loginText = new EditText(MenuActivity.this);
            final EditText passText = new EditText(MenuActivity.this);
            loginText.setHint("Login");
            passText.setHint("Hasło");
            LinearLayout ll=new LinearLayout(MenuActivity.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(loginText);
            ll.addView(passText);
            AlertDialog dialogShowItem = new AlertDialog.Builder(MenuActivity.this)
                    .setTitle("Podaj login i hasło")
                    .setView(ll)
                    .setPositiveButton("Potwierdź",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteUSer(loginText.getText().toString(),passText.getText().toString());
                        }
                    })
                    .setNegativeButton("Anuluj", null).create();
            dialogShowItem.show();


        }
        else return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button listButton = (Button) findViewById(R.id.listButton);
        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);

        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToListActivity();

            }

        });

        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToCalendarActivity();

            }

        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToScheduleActivity();

            }

        });



    }

    private void goToListActivity() {

        Intent intent = new Intent(MenuActivity.this, ListActivity.class);

        startActivity(intent);

    }

    private void goToCalendarActivity() {

        Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);

        startActivity(intent);

    }

    private void goToScheduleActivity() {

        Intent intent = new Intent(MenuActivity.this, ScheduleSubjectListActivity.class);
        Bundle b = new Bundle();
        b.putString("day","Monday");
        intent.putExtras(b);
        startActivity(intent);

    }

    private void deleteUSer(String login, String password){
        if(login.equals("") || password.equals(""))
            Toast.makeText(MenuActivity.this, "Błąd", Toast.LENGTH_LONG).show();
        else {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), password);

            firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MenuActivity.this, "Konto usunięte", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(MenuActivity.this, "Błąd", Toast.LENGTH_LONG).show();
                        }
                    });
                    }
                    else
                        Toast.makeText(MenuActivity.this, "Błąd", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
