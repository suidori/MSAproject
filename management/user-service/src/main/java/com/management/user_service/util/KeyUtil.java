package com.management.user_service.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtil {

    // 공개 키 로드
    public static PublicKey getPublicKey() throws Exception {
        String key = loadKey("keys/public.key");  // 공개 키 경로
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
