package com.uacm.cm.puntodeventa.controlador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.uacm.cm.puntodeventa.R;
import com.uacm.cm.puntodeventa.fragment.AcercaDeFragment;
import com.uacm.cm.puntodeventa.fragment.InicioFragment;
import com.uacm.cm.puntodeventa.fragment.ProductosFragment;
import com.uacm.cm.puntodeventa.fragment.VentasFragment;
import com.uacm.cm.puntodeventa.modelo.Usuario;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private Usuario usuario;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        usuario = (Usuario)getIntent().getSerializableExtra("usuario");

        // Configuracion de la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializacion del drawer (menu lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);  // Menu lateral

        // Inicializacion del BottomNavigationView (menu inferior)
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);  // Menu inferior

        // Configuracion del ActionBarDrawerToggle para el menu lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir_nav, R.string.cerrar_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        String mostrarFragmento = getIntent().getStringExtra("mostrarFragmento");
        if(savedInstanceState == null){
            if("productos".equals(mostrarFragmento)){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, new ProductosFragment()).commit();
                navigationView.setCheckedItem(R.id.btn_productos);
                bottomNavigationView.setSelectedItemId(R.id.btn_productos);
            }else{
                InicioFragment inicioFragment = new InicioFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario", usuario);  // Pasa el objeto Usuario
                inicioFragment.setArguments(bundle);
                // Reemplaza el fragmento en el contenedor
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, inicioFragment)
                        .commit();
                navigationView.setCheckedItem(R.id.home);
            }
        }
    }
    private boolean isHandlingSelection = false;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (isHandlingSelection) return false;
        isHandlingSelection = true;
        int id = item.getItemId();
        if (id == R.id.nav_mis_productos || id == R.id.btn_productos) {
            ProductosFragment productosFragment = new ProductosFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("usuario", usuario);
            productosFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, productosFragment)
                    .addToBackStack(null)
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_mis_productos);
            bottomNavigationView.setSelectedItemId(R.id.btn_productos);
        } else if (id == R.id.nav_mis_ventas || id == R.id.btn_ventas) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new VentasFragment())
                    .addToBackStack(null)
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_mis_ventas);
            bottomNavigationView.setSelectedItemId(R.id.btn_ventas);

        } else if (id == R.id.nav_about || id == R.id.btn_acerca_de) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new AcercaDeFragment())
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_about);
            bottomNavigationView.setSelectedItemId(R.id.btn_acerca_de);

        }  else if (id == R.id.home || id == R.id.btn_inicio) {
            InicioFragment inicioFragment = new InicioFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("usuario", usuario);
            inicioFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, inicioFragment)
                    .commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.home);
            bottomNavigationView.setSelectedItemId(R.id.btn_inicio);
        } else if (id == R.id.cerrarSesion){
            SharedPreferences sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("sesionIniciada", false);  // Cambia el valor a false
            editor.putInt("ID", -1);
            editor.apply();
            Intent intenCerrarSesion = new Intent(this, Login.class);
            startActivity(intenCerrarSesion);
            finish();
        }else if(id == R.id.btn_estadistica || id == R.id.nav_estadisticas){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new EstadisticasProductos()).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_estadisticas);
            bottomNavigationView.setSelectedItemId(R.id.btn_estadistica);
        }
        else {
            Log.d("Inicio", "id no reconocido");
        }
        isHandlingSelection = false;
        return true;
    }

    @Override
    public void onBackPressed() {
        // Si el menu lateral esta abierto, lo cerramos primero
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                if (backToast != null) backToast.cancel();
                SharedPreferences sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("sesionIniciada", true);
                editor.apply();
                finishAffinity();
            } else {
                backToast = Toast.makeText(getBaseContext(), "Presiona de nuevo para salir", Toast.LENGTH_SHORT);
                backToast.show();
                backPressedTime = System.currentTimeMillis();
            }
        }
    }
}
