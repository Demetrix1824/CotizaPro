package com.example.cotizapro;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public final class SessionStore {

    private static final String PREFS_NAME = "CotizaProSession";

    private static final String KEY_ALIAS = "CotizaProSessionKey";

    private static final String KEY_IV = "token_iv";

    private static final String KEY_TOKEN = "token_encrypted";

    private SessionStore() {
        // Evitar crear instancias de esta clase
    }

    // OBTENER O CREAR CLAVE DE CIFRADO
    private static SecretKey obtenerClave() throws Exception {

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

        keyStore.load(null);

        if (keyStore.containsAlias(KEY_ALIAS)) {

            return (SecretKey) keyStore.getKey(KEY_ALIAS, null);

        }

        KeyGenerator generador = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
        );

        KeyGenParameterSpec parametros =
                new KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT
                )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_NONE
                        )
                        .setKeySize(256)
                        .build();

        generador.init(parametros);

        return generador.generateKey();
    }

    // GUARDAR TOKEN CIFRADO
    public static void guardarToken(
            Context context,
            String token
    ) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(
                Cipher.ENCRYPT_MODE,
                obtenerClave()
        );

        byte[] tokenCifrado = cipher.doFinal(
                token.getBytes(StandardCharsets.UTF_8)
        );

        byte[] iv = cipher.getIV();

        SharedPreferences preferencias =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        preferencias.edit()
                .putString(
                        KEY_IV,
                        Base64.encodeToString(iv, Base64.NO_WRAP)
                )
                .putString(
                        KEY_TOKEN,
                        Base64.encodeToString(
                                tokenCifrado,
                                Base64.NO_WRAP
                        )
                )
                .apply();
    }

    // OBTENER Y DESCIFRAR TOKEN
    public static String obtenerToken(Context context)
            throws Exception {

        SharedPreferences preferencias =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        String ivGuardado = preferencias.getString(KEY_IV, null);

        String tokenGuardado = preferencias.getString(
                KEY_TOKEN,
                null
        );

        if (ivGuardado == null || tokenGuardado == null) {
            return null;
        }

        byte[] iv = Base64.decode(ivGuardado, Base64.NO_WRAP);

        byte[] tokenCifrado = Base64.decode(
                tokenGuardado,
                Base64.NO_WRAP
        );

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(
                Cipher.DECRYPT_MODE,
                obtenerClave(),
                new GCMParameterSpec(128, iv)
        );

        byte[] tokenDescifrado = cipher.doFinal(tokenCifrado);

        return new String(
                tokenDescifrado,
                StandardCharsets.UTF_8
        );
    }

    // ELIMINAR SESION LOCAL Y CLAVE DE CIFRADO
    public static void limpiarSesion(Context context) {

        SharedPreferences preferencias =
                context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        preferencias.edit().clear().apply();

        try {

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

            keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {
                keyStore.deleteEntry(KEY_ALIAS);
            }

        } catch (Exception e) {

            android.util.Log.e(
                    "SessionStore",
                    "No se pudo eliminar la clave de sesión",
                    e
            );

        }
    }
}