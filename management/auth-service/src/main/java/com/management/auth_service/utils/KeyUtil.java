package com.management.auth_service.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyUtil {

    public static PrivateKey getPrivateKey() throws Exception {
        String key = loadKey("/app/keys/private.pem");  // Kubernetes에서 마운트된 경로
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey() throws Exception {
        String key = loadKey("/app/keys/public.key");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private static String loadKey(String path) throws IOException {
        // 파일 시스템에서 키 파일을 읽음
        String key = new String(Files.readAllBytes(Paths.get(path)))
                .replaceAll("-----BEGIN [A-Z ]+-----", "")  // PEM 헤더 제거
                .replaceAll("-----END [A-Z ]+-----", "")    // PEM 푸터 제거
                .replaceAll("\\s", "");  // 공백 제거
        return key;
    }
}
