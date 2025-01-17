package com.example.myclientapp.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myclientapp.MainActivity;
import com.example.myclientapp.R;
import com.example.myclientapp.bbdd.DataBase;
import com.example.myclientapp.notas.NotasActivity;
import com.example.myclientapp.notas.NuevaNotaFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
  TextView verNom, verTel, verDir, verEmail, verOtro;
  Button btnEditar, btnVolver;
    Fragment fragmentEditaCl;
    FragmentTransaction fragmentTransaction;
    LinearLayout viewCliente;
    Cliente clModelo;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarVista();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_borrar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId()==R.id.delete){
            DataBase db= new DataBase(this);
            Intent intent= getIntent();
            id=intent.getIntExtra("ID", 0);
            db.eliminaCliente(id);
            Toast.makeText(getApplicationContext(), "Cliente eliminado", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ClientActivity.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    protected void cargarVista(){
        setContentView(R.layout.activity_client);
        getSupportActionBar().setTitle("Detalle del Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewCliente = (LinearLayout)findViewById(R.id.layout_contenedor);

        verNom = findViewById(R.id.tvNom);
        verDir = findViewById(R.id.tvDir);
        verTel = findViewById(R.id.tvTel);
        verEmail = findViewById(R.id.tvEmail);
        verOtro = findViewById(R.id.tvOtro);
        btnEditar = findViewById(R.id.btn_editar);
        btnVolver = findViewById(R.id.btn_volver);

        DataBase db = new DataBase(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);
        clModelo = db.getClientes(id);

        verNom.setText(clModelo.getNombre());
        verDir.setText(clModelo.getDireccion());
        verTel.setText(clModelo.getTelefono());
        verEmail.setText(clModelo.getEmail());
        verOtro.setText(clModelo.getOtro());
        Toast.makeText(getApplicationContext(), "id " + clModelo.getId(), Toast.LENGTH_SHORT).show();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void verNotas(View view){
        Intent intent = new Intent(ClientActivity.this, NotasActivity.class);
        intent.putExtra("id_cl", clModelo.getId());
        startActivity(intent);
    }
    public void addNota(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("idCl",clModelo.getId());
        Log.i("id cl en ClientActivity",String.valueOf(clModelo.getId()));
       NuevaNotaFragment fragmentNuevaNota = new NuevaNotaFragment();
       fragmentNuevaNota.setArguments(bundle);
       fragmentTransaction=getSupportFragmentManager().beginTransaction();
       fragmentTransaction.replace(R.id.contenedor_nuevaNota,fragmentNuevaNota);
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();
       viewCliente.setVisibility(View.INVISIBLE);
    }
    public void editClient(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);

        fragmentEditaCl= new EditaClienteFragment();
        fragmentEditaCl.setArguments(bundle);
        fragmentTransaction=getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_editClient,fragmentEditaCl);
        fragmentTransaction.addToBackStack(null)
                .commit();

        viewCliente.setVisibility(View.INVISIBLE);
    }
}