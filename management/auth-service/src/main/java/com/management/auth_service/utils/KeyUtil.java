package com.management.auth_service.utils;

import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;

public class KeyUtil {

    public static PrivateKey getPrivateKey() throws Exception {
        String key = loadKey("keys/private.pem");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey() throws Exception {
        String key = loadKey("keys/public.key");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private static String loadKey(String path) throws IOException {
        return new String(Files.readAllBytes(new ClassPathResource(path).getFile().toPath()))
                .replaceAll("-----[A-Z ]+-----", "") // 키 헤더 제거
                .replaceAll("\\s", ""); // 공백 제거
    }
}

