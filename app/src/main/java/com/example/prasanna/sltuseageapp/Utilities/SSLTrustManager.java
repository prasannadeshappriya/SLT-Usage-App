package com.example.prasanna.sltuseageapp.Utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.prasanna.sltuseageapp.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by prasanna on 6/10/17.
 */

public class SSLTrustManager {
    /**
     * https://developer.android.com/training/articles/security-ssl.html
     */
    @Nullable
    public static SSLSocketFactory setup(Context context){
        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().openRawResource(R.raw.comodorsadomainvalidationsecureserverca);
            Certificate ca;

            try {
                ca = cf.generateCertificate(caInput);
                //System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        }
        catch (Exception e) {
            Log.i("TEST","SSLTrustManager Failed");
        }

        return null;
    }
}
