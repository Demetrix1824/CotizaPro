package com.example.cotizapro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.content.pm.PackageManager;
import android.os.Build;

import android.util.Log;


public class MainActivity extends AppCompatActivity {

    // ELEMENTOS DE LA INTERFAZ
    EditText etUsuario, etContrasena;

    Button btnIniciarSesion;

    TextView tvRecuperar;

    // DIRECCION DE LA API PHP
    private static final String URL_LOGIN =
            "http://192.168.1.75/cotizapro_api/login.php";

    // DIRECCION PARA VALIDAR LA SESION
    private static final String URL_VERIFY =
            "http://192.168.1.75/cotizapro_api/verify_session.php";


    // PERMISO PARA ACCEDER A LA RED LOCAL
    private static final String PERMISO_RED_LOCAL =
            "android.permission.ACCESS_LOCAL_NETWORK";

    private static final int CODIGO_PERMISO_RED_LOCAL = 1001;


    // EJECUTOR PARA SOLICITUDES DE INTERNET
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RELACIONAR ELEMENTOS XML
        etUsuario = findViewById(R.id.etUsuario);

        etContrasena = findViewById(R.id.etContrasena);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        tvRecuperar = findViewById(R.id.tvRecuperar);




        // EVITAR QUE SE INICIE SESION MANUALMENTE
// MIENTRAS SE COMPRUEBA UNA SESION ANTERIOR
        btnIniciarSesion.setEnabled(false);

// COMPROBAR SI YA EXISTE UNA SESION
        if (Build.VERSION.SDK_INT >= 37 &&
                checkSelfPermission(
                        "android.permission.ACCESS_LOCAL_NETWORK"
                ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            "android.permission.ACCESS_LOCAL_NETWORK"
                    },
                    1001
            );

        } else {

            validarSesionGuardada();

        }


/*
// COMPROBAR PERMISO DE RED LOCAL
        if (Build.VERSION.SDK_INT >= 37) {

            if (checkSelfPermission(PERMISO_RED_LOCAL)
                    == PackageManager.PERMISSION_GRANTED) {

                // El permiso ya está autorizado
                probarConexionServidor();

            } else {

                // Solicitar permiso al usuario
                requestPermissions(
                        new String[]{PERMISO_RED_LOCAL},
                        CODIGO_PERMISO_RED_LOCAL
                );

            }

        } else {

            // Para versiones anteriores a Android 17
            probarConexionServidor();

        }
*/



        // EVENTO DEL BOTON INICIAR SESION
        btnIniciarSesion.setOnClickListener(v -> {

            String usuario = etUsuario.getText()
                    .toString().trim();

            String contrasena = etContrasena.getText()
                    .toString();

            // VALIDAR USUARIO
            if (usuario.isEmpty()) {

                etUsuario.setError("Ingresa tu usuario");
                etUsuario.requestFocus();

                return;
            }

            // VALIDAR CONTRASEÑA
            if (contrasena.isEmpty()) {

                etContrasena.setError("Ingresa tu contraseña");
                etContrasena.requestFocus();

                return;
            }

            // COMPROBAR PERMISO ANTES DE INICIAR SESION
            if (Build.VERSION.SDK_INT >= 37 &&
                    checkSelfPermission(PERMISO_RED_LOCAL)
                            != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{PERMISO_RED_LOCAL},
                        CODIGO_PERMISO_RED_LOCAL
                );

                return;
            }

// CONECTAR CON PHP
            iniciarSesionAPI(usuario, contrasena);

        });

        // RECUPERACION DE CONTRASEÑA
        tvRecuperar.setOnClickListener(v -> {

            Toast.makeText(
                    MainActivity.this,
                    "Contacta al administrador para recuperar tu contraseña.",
                    Toast.LENGTH_LONG
            ).show();

        });

    }

    // METODO DE CONEXION CON LA API PHP
    private void iniciarSesionAPI(
            String usuario,
            String contrasena
    ) {

        btnIniciarSesion.setEnabled(false);

        executor.execute(() -> {

            HttpURLConnection conexion = null;

            try {

                URL url = new URL(URL_LOGIN);

                Log.d("CotizaProLogin", "URL utilizada: " + URL_LOGIN);

                conexion = (HttpURLConnection)
                        url.openConnection();

                conexion.setRequestMethod("POST");

                conexion.setConnectTimeout(10000);

                conexion.setReadTimeout(10000);

                conexion.setDoOutput(true);

                conexion.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                conexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                // PREPARAR DATOS JSON
                JSONObject datos = new JSONObject();

                datos.put("usuario", usuario);

                datos.put("contrasena", contrasena);

                // ENVIAR DATOS A PHP
                try (OutputStream salida = conexion.getOutputStream()) {

                    salida.write(
                            datos.toString().getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

                }

                // OBTENER RESPUESTA
                int codigo = conexion.getResponseCode();

                InputStream respuestaStream;

                if (codigo >= 200 && codigo < 300) {

                    respuestaStream = conexion.getInputStream();

                } else {

                    respuestaStream = conexion.getErrorStream();

                }

                if (respuestaStream == null) {

                    throw new Exception(
                            "El servidor no devolvió una respuesta."
                    );

                }

                StringBuilder respuesta = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                respuestaStream,
                                StandardCharsets.UTF_8
                        )
                )) {

                    String linea;

                    while ((linea = reader.readLine()) != null) {

                        respuesta.append(linea);

                    }

                }

                // LEER RESPUESTA JSON
                JSONObject resultado = new JSONObject(
                        respuesta.toString()
                );

                final boolean correcto = codigo == 200 &&
                        resultado.optBoolean("ok", false);





                // GUARDAR TOKEN CUANDO EL LOGIN SEA CORRECTO
                if (correcto) {

                    String token = resultado.optString("token", "");

                    if (token.isEmpty()) {
                        throw new Exception(
                                "El servidor no devolvió un token de sesión."
                        );
                    }

                    SessionStore.guardarToken(
                            getApplicationContext(),
                            token
                    );

                }












                final String mensaje = resultado.optString(
                        "mensaje",
                        "No se pudo iniciar sesión."
                );

                final String idUsuario = resultado.optString(
                        "id",
                        ""
                );

                final String nombre = resultado.optString(
                        "nombre",
                        ""
                );

                final String usuarioRespuesta = resultado.optString(
                        "usuario",
                        usuario
                );

                final String tipoUsuario = resultado.optString(
                        "tipo_usuario",
                        ""
                );

                // ACTUALIZAR INTERFAZ
                runOnUiThread(() -> {

                    btnIniciarSesion.setEnabled(true);

                    if (correcto) {

                        // ABRIR HOME ACTIVITY
                        abrirHome(
                                idUsuario,
                                nombre,
                                usuarioRespuesta,
                                tipoUsuario
                        );

                    } else {

                        Toast.makeText(
                                MainActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

            } catch (Exception e) {

                Log.e("CotizaProLogin", "ERROR DE CONEXION", e);

                String detalle = e.getClass().getSimpleName()
                        + " - " + e.getMessage();

                runOnUiThread(() -> {

                    btnIniciarSesion.setEnabled(true);

                    Toast.makeText(
                            MainActivity.this,
                            "Error: " + detalle,
                            Toast.LENGTH_LONG
                    ).show();

                });

            } finally {

                if (conexion != null) {
                    conexion.disconnect();
                }

            }

        });

    }



    // COMPROBAR SI EXISTE UNA SESION GUARDADA
    private void validarSesionGuardada() {

        executor.execute(() -> {

            HttpURLConnection conexion = null;

            try {

                // RECUPERAR TOKEN DEL CELULAR
                String token = SessionStore.obtenerToken(
                        getApplicationContext()
                );

                // SI NO EXISTE TOKEN, MOSTRAR LOGIN
                if (token == null || token.isEmpty()) {

                    runOnUiThread(() -> {
                        btnIniciarSesion.setEnabled(true);
                    });

                    return;
                }

                // CONECTAR CON PHP
                URL url = new URL(URL_VERIFY);

                conexion = (HttpURLConnection) url.openConnection();

                conexion.setRequestMethod("GET");

                conexion.setConnectTimeout(15000);

                conexion.setReadTimeout(15000);

                conexion.setRequestProperty(
                        "Authorization",
                        "Bearer " + token
                );

                conexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                // OBTENER RESPUESTA
                int codigo = conexion.getResponseCode();

                InputStream stream;

                if (codigo >= 200 && codigo < 300) {

                    stream = conexion.getInputStream();

                } else {

                    stream = conexion.getErrorStream();

                }

                if (stream == null) {
                    throw new Exception("El servidor no devolvió una respuesta.");
                }

                StringBuilder respuesta = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                stream,
                                StandardCharsets.UTF_8
                        )
                )) {

                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }

                }

                JSONObject resultado = new JSONObject(
                        respuesta.toString()
                );

                boolean sesionValida = codigo == 200 &&
                        resultado.optBoolean("ok", false);

                // SI EL SERVIDOR CONFIRMA LA SESION
                if (sesionValida) {

                    String idUsuario = resultado.optString("id", "");
                    String nombre = resultado.optString("nombre", "");
                    String usuario = resultado.optString("usuario", "");
                    String tipoUsuario = resultado.optString(
                            "tipo_usuario",
                            ""
                    );

                    runOnUiThread(() -> {

                        abrirHome(
                                idUsuario,
                                nombre,
                                usuario,
                                tipoUsuario
                        );

                    });

                } else if (codigo == 401) {

                    // TOKEN EXPIRADO O REVOCADO
                    SessionStore.limpiarSesion(
                            getApplicationContext()
                    );

                    runOnUiThread(() -> {

                        btnIniciarSesion.setEnabled(true);

                        Toast.makeText(
                                MainActivity.this,
                                "Tu sesión ha expirado. Inicia sesión nuevamente.",
                                Toast.LENGTH_LONG
                        ).show();

                    });

                } else {

                    throw new Exception(
                            resultado.optString(
                                    "mensaje",
                                    "No se pudo validar la sesión."
                            )
                    );

                }

            } catch (Exception e) {

                android.util.Log.e(
                        "CotizaProSession",
                        "Error al validar la sesión",
                        e
                );

                runOnUiThread(() -> {

                    btnIniciarSesion.setEnabled(true);

                    Toast.makeText(
                            MainActivity.this,
                            "No se pudo comprobar tu sesión. Verifica tu conexión con el servidor.",
                            Toast.LENGTH_LONG
                    ).show();

                });

            } finally {

                if (conexion != null) {
                    conexion.disconnect();
                }

            }

        });

    }




    // ABRIR LA PANTALLA PRINCIPAL
    private void abrirHome(
            String idUsuario,
            String nombre,
            String usuario,
            String tipoUsuario
    ) {

        Intent intent = new Intent(
                MainActivity.this,
                HomeActivity.class
        );

        intent.putExtra("idUsuario", idUsuario);

        intent.putExtra("nombre", nombre);

        intent.putExtra("usuario", usuario);

        intent.putExtra("tipoUsuario", tipoUsuario);

        startActivity(intent);

        finish();

    }








    // PRUEBA DE CONEXION DESDE ANDROID
    private void probarConexionServidor() {

        executor.execute(() -> {

            HttpURLConnection conexion = null;

            try {

                URL url = new URL(
                        "http://192.168.1.75/cotizapro_api/health.php"
                );

                Log.d("CotizaProHealth", "Conectando a: " + url);

                conexion = (HttpURLConnection) url.openConnection();

                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(15000);
                conexion.setReadTimeout(15000);
                conexion.setUseCaches(false);

                int codigo = conexion.getResponseCode();

                InputStream stream = conexion.getInputStream();

                StringBuilder respuesta = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                stream,
                                StandardCharsets.UTF_8
                        )
                )) {

                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }

                }

                Log.d(
                        "CotizaProHealth",
                        "HTTP: " + codigo + " | " + respuesta
                );

            } catch (Exception e) {

                Log.e(
                        "CotizaProHealth",
                        "ERROR EN LA PRUEBA",
                        e
                );

            } finally {

                if (conexion != null) {
                    conexion.disconnect();
                }

            }

        });

    }


/*
    // RESPUESTA DEL PERMISO DE RED LOCAL
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == CODIGO_PERMISO_RED_LOCAL) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(
                        MainActivity.this,
                        "Permiso de red local concedido.",
                        Toast.LENGTH_SHORT
                ).show();

                // Probar conexión con XAMPP
                probarConexionServidor();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Necesitas permitir el acceso a la red local para utilizar CotizaPro.",
                        Toast.LENGTH_LONG
                ).show();

            }

        }

    }*/



    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == 1001) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                validarSesionGuardada();

            } else {

                btnIniciarSesion.setEnabled(true);

                Toast.makeText(
                        this,
                        "Debes permitir el acceso a la red local para utilizar CotizaPro.",
                        Toast.LENGTH_LONG
                ).show();

            }

        }

    }






/*

    // COMPROBAR SI EXISTE UNA SESION GUARDADA
    private void validarSesionGuardada() {

        executor.execute(() -> {

            HttpURLConnection conexion = null;

            try {

                // RECUPERAR TOKEN DEL CELULAR
                String token = SessionStore.obtenerToken(
                        getApplicationContext()
                );

                // SI NO EXISTE TOKEN, MOSTRAR LOGIN
                if (token == null || token.isEmpty()) {

                    runOnUiThread(() -> {
                        btnIniciarSesion.setEnabled(true);
                    });

                    return;
                }

                // CONECTAR CON PHP
                URL url = new URL(URL_VERIFY);

                conexion = (HttpURLConnection) url.openConnection();

                conexion.setRequestMethod("GET");

                conexion.setConnectTimeout(15000);

                conexion.setReadTimeout(15000);

                conexion.setRequestProperty(
                        "Authorization",
                        "Bearer " + token
                );

                conexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                // OBTENER RESPUESTA
                int codigo = conexion.getResponseCode();

                InputStream stream;

                if (codigo >= 200 && codigo < 300) {

                    stream = conexion.getInputStream();

                } else {

                    stream = conexion.getErrorStream();

                }

                if (stream == null) {
                    throw new Exception("El servidor no devolvió una respuesta.");
                }

                StringBuilder respuesta = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                stream,
                                StandardCharsets.UTF_8
                        )
                )) {

                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }

                }

                JSONObject resultado = new JSONObject(
                        respuesta.toString()
                );

                boolean sesionValida = codigo == 200 &&
                        resultado.optBoolean("ok", false);

                // SI EL SERVIDOR CONFIRMA LA SESION
                if (sesionValida) {

                    String idUsuario = resultado.optString("id", "");
                    String nombre = resultado.optString("nombre", "");
                    String usuario = resultado.optString("usuario", "");
                    String tipoUsuario = resultado.optString(
                            "tipo_usuario",
                            ""
                    );

                    runOnUiThread(() -> {

                        abrirHome(
                                idUsuario,
                                nombre,
                                usuario,
                                tipoUsuario
                        );

                    });

                } else if (codigo == 401) {

                    // TOKEN EXPIRADO O REVOCADO
                    SessionStore.limpiarSesion(
                            getApplicationContext()
                    );

                    runOnUiThread(() -> {

                        btnIniciarSesion.setEnabled(true);

                        Toast.makeText(
                                MainActivity.this,
                                "Tu sesión ha expirado. Inicia sesión nuevamente.",
                                Toast.LENGTH_LONG
                        ).show();

                    });

                } else {

                    throw new Exception(
                            resultado.optString(
                                    "mensaje",
                                    "No se pudo validar la sesión."
                            )
                    );

                }

            } catch (Exception e) {

                android.util.Log.e(
                        "CotizaProSession",
                        "Error al validar la sesión",
                        e
                );

                runOnUiThread(() -> {

                    btnIniciarSesion.setEnabled(true);

                    Toast.makeText(
                            MainActivity.this,
                            "No se pudo comprobar tu sesión. Verifica tu conexión con el servidor.",
                            Toast.LENGTH_LONG
                    ).show();

                });

            } finally {

                if (conexion != null) {
                    conexion.disconnect();
                }

            }

        });

    }*/


    @Override
    protected void onDestroy() {

        super.onDestroy();

        executor.shutdown();

    }

}