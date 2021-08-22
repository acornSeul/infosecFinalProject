package com.example.team5_final.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class CipherPass {
    private String password;

    @SneakyThrows
    public byte[] encrypt(){
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DES");
        generator.initialize(1024);

        KeyPair pair = generator.generateKeyPair();
        PublicKey publicKey = pair.getPublic();

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] in_pw = password.getBytes();
        cipher.update(in_pw);

        byte[] encrypt_pw = cipher.doFinal();

        return encrypt_pw;
    }
}
