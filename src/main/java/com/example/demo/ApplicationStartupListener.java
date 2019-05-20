package com.example.demo;

import com.mastercard.sdk.core.MasterCardApiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;


@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${masterpass.consumerKey}")
    String consumerKey;


    @Value("${masterpass.key.path}")
    String keyPath;


    @Value("${masterpass.key.password}")
    String keyPassword;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("Initializing Masterpass APIs");


        MasterCardApiConfig.setSandBox (true);
        MasterCardApiConfig.setConsumerKey(consumerKey);
        MasterCardApiConfig.setPrivateKey (getPrivateKey());

        System.out.println("Masterpass APIs Initialized");
    }


    private PrivateKey getPrivateKey(){

        PrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load (new FileInputStream(new File(keyPath)),
                    keyPassword.toCharArray ());
            Enumeration aliases = ks.aliases ();
            String keyAlias = "";

            while (aliases.hasMoreElements ()){
                keyAlias = (String) aliases.nextElement ();
            }

            privateKey = (PrivateKey)ks.getKey(keyAlias, keyPassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return privateKey;

    }
}
