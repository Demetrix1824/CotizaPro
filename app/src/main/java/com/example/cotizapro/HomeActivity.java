package com.example.cotizapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView btnMenu;
    TextView tvUsuarioHome;

    TextView navInicio;
    TextView navNuevaCotizacion;
    TextView navClientes;
    TextView navProductos;
    TextView navHistorial;
    TextView navConfiguracion;
    TextView navAyuda;
    TextView navCerrarSesion;

    LinearLayout cardNuevaCotizacion;
    LinearLayout cardClientes;
    LinearLayout cardProductos;
    LinearLayout cardHistorial;

    // DATOS DEL USUARIO
    String idUsuario;
    String nombre;
    String usuario;
    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // REFERENCIA DEL DRAWERLAYOUT
        drawerLayout = findViewById(R.id.drawerLayout);

        // CORREGIR ESPACIO DE LA BARRA DE NOTIFICACIONES
        View contenidoPrincipal = drawerLayout.getChildAt(0);
        View menuLateral = drawerLayout.getChildAt(1);

        final int contenidoTop = contenidoPrincipal.getPaddingTop();
        final int menuTop = menuLateral.getPaddingTop();

        ViewCompat.setOnApplyWindowInsetsListener(
                drawerLayout,
                (view, windowInsets) -> {

                    Insets barras = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.displayCutout()
                    );

                    contenidoPrincipal.setPadding(
                            contenidoPrincipal.getPaddingLeft(),
                            contenidoTop + barras.top,
                            contenidoPrincipal.getPaddingRight(),
                            contenidoPrincipal.getPaddingBottom()
                    );

                    menuLateral.setPadding(
                            menuLateral.getPaddingLeft(),
                            menuTop + barras.top,
                            menuLateral.getPaddingRight(),
                            menuLateral.getPaddingBottom()
                    );

                    return windowInsets;
                }
        );

        ViewCompat.requestApplyInsets(drawerLayout);

        // REFERENCIAS DEL MENU LATERAL
        btnMenu = findViewById(R.id.btnMenu);
        tvUsuarioHome = findViewById(R.id.tvUsuarioHome);

        navInicio = findViewById(R.id.navInicio);
        navNuevaCotizacion = findViewById(R.id.navNuevaCotizacion);
        navClientes = findViewById(R.id.navClientes);
        navProductos = findViewById(R.id.navProductos);
        navHistorial = findViewById(R.id.navHistorial);
        navConfiguracion = findViewById(R.id.navConfiguracion);
        navAyuda = findViewById(R.id.navAyuda);
        navCerrarSesion = findViewById(R.id.navCerrarSesion);

        // REFERENCIAS DE LOS ACCESOS RAPIDOS
        cardNuevaCotizacion = findViewById(R.id.cardNuevaCotizacion);
        cardClientes = findViewById(R.id.cardClientes);
        cardProductos = findViewById(R.id.cardProductos);
        cardHistorial = findViewById(R.id.cardHistorial);

        // RECIBIR INFORMACION DESDE MAINACTIVITY
        idUsuario = getIntent().getStringExtra("idUsuario");
        nombre = getIntent().getStringExtra("nombre");
        usuario = getIntent().getStringExtra("usuario");
        tipoUsuario = getIntent().getStringExtra("tipoUsuario");

        // MOSTRAR NOMBRE DEL USUARIO
        if (nombre != null && !nombre.isEmpty()) {

            tvUsuarioHome.setText("Bienvenido, " + nombre);

        } else if (usuario != null && !usuario.isEmpty()) {

            tvUsuarioHome.setText("Usuario: " + usuario);

        } else {

            tvUsuarioHome.setText("Bienvenido a CotizaPro");

        }

        // HABILITAR MENU LATERAL
        drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_UNLOCKED
        );

        // ABRIR Y CERRAR MENU
        btnMenu.setOnClickListener(v -> {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                drawerLayout.closeDrawer(GravityCompat.START);

            } else {

                drawerLayout.openDrawer(GravityCompat.START);

            }

        });

        // OPCION INICIO
        navInicio.setOnClickListener(v -> {

            drawerLayout.closeDrawer(GravityCompat.START);

        });

        // OPCIONES DEL MENU LATERAL
        configurarBoton(navNuevaCotizacion, "Nueva cotización");
        configurarBoton(navClientes, "Clientes");
        configurarBoton(navProductos, "Productos");
        configurarBoton(navHistorial, "Historial de cotizaciones");
        configurarBoton(navConfiguracion, "Configuración");
        configurarBoton(navAyuda, "Ayuda");

        // ACCESOS RAPIDOS
        configurarBoton(cardNuevaCotizacion, "Nueva cotización");
        configurarBoton(cardClientes, "Clientes");
        configurarBoton(cardProductos, "Productos");
        configurarBoton(cardHistorial, "Historial de cotizaciones");


        /*
        // CERRAR SESION
        navCerrarSesion.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Sesión cerrada correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    HomeActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();

        }); */


        // CERRAR SESION
        navCerrarSesion.setOnClickListener(v -> {

            // RECUPERAR TOKEN ANTES DE ELIMINARLO
            String token = null;

            try {

                token = SessionStore.obtenerToken(
                        getApplicationContext()
                );

            } catch (Exception e) {

                android.util.Log.e(
                        "CotizaProSession",
                        "No se pudo recuperar el token",
                        e
                );

            }

            // ELIMINAR SESION DEL CELULAR
            SessionStore.limpiarSesion(
                    getApplicationContext()
            );

            // REVOCAR TOKEN EN EL SERVIDOR
            if (token != null) {

                String tokenFinal = token;

                new Thread(() -> {

                    HttpURLConnection conexion = null;

                    try {

                        URL url = new URL(
                                "http://192.168.1.75/cotizapro_api/logout.php"
                        );

                        conexion = (HttpURLConnection)
                                url.openConnection();

                        conexion.setRequestMethod("POST");

                        conexion.setConnectTimeout(5000);

                        conexion.setReadTimeout(5000);

                        conexion.setRequestProperty(
                                "Authorization",
                                "Bearer " + tokenFinal
                        );

                        conexion.getResponseCode();

                    } catch (Exception e) {

                        android.util.Log.e(
                                "CotizaProSession",
                                "No se pudo revocar la sesión en el servidor",
                                e
                        );

                    } finally {

                        if (conexion != null) {
                            conexion.disconnect();
                        }

                    }

                }).start();

            }

            Toast.makeText(
                    HomeActivity.this,
                    "Sesión cerrada correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            // REGRESAR AL LOGIN
            Intent intent = new Intent(
                    HomeActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();

        });

    }

    // CONFIGURAR ACCIONES DE LOS BOTONES
    private void configurarBoton(View boton, String modulo) {

        boton.setOnClickListener(v -> {

            // CERRAR MENU LATERAL
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                drawerLayout.closeDrawer(GravityCompat.START);

            }

            // MENSAJE TEMPORAL
            Toast.makeText(
                    HomeActivity.this,
                    "Módulo: " + modulo,
                    Toast.LENGTH_SHORT
            ).show();

        });

    }

}