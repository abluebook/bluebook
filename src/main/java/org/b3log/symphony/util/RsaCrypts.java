/*
 * Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
 * Copyright (C) 2012-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.util;

import org.apache.commons.lang3.StringUtils;
import org.b3log.latke.Latkes;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA utilities.
 */
public final class RsaCrypts {

    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final PrivateKey PRIVATE_KEY;
    private static final PublicKey PUBLIC_KEY;
    private static final String PUBLIC_KEY_PEM;

    static {
        try {
            final String privatePem = loadPem("RSA_PRIVATE_KEY", "RSA_PRIVATE_KEY_PATH");
            final String publicPem = loadPem("RSA_PUBLIC_KEY", "RSA_PUBLIC_KEY_PATH");
            if (StringUtils.isNotBlank(privatePem) && StringUtils.isNotBlank(publicPem)) {
                PRIVATE_KEY = readPrivateKey(privatePem);
                PUBLIC_KEY = readPublicKey(publicPem);
                PUBLIC_KEY_PEM = toPublicPem(PUBLIC_KEY.getEncoded());
            } else {
                if (productionRuntime()) {
                    throw new IllegalStateException("RSA_PRIVATE_KEY_PATH/RSA_PUBLIC_KEY_PATH or RSA_PRIVATE_KEY/RSA_PUBLIC_KEY must be configured in production");
                }

                final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                final KeyPair keyPair = generator.generateKeyPair();
                PRIVATE_KEY = keyPair.getPrivate();
                PUBLIC_KEY = keyPair.getPublic();
                PUBLIC_KEY_PEM = toPublicPem(PUBLIC_KEY.getEncoded());
            }
        } catch (final Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private RsaCrypts() {
    }

    public static String publicKeyPem() {
        return PUBLIC_KEY_PEM;
    }

    public static String decryptPassword(final String cipherText) {
        if (StringUtils.isBlank(cipherText)) {
            throw new IllegalArgumentException("Blank encrypted password");
        }

        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invalid encrypted password", e);
        }
    }

    static String encryptForTest(final String plainText) throws Exception {
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    private static boolean productionRuntime() {
        try {
            return Latkes.RuntimeMode.PRODUCTION == Latkes.getRuntimeMode();
        } catch (final RuntimeException e) {
            return false;
        }
    }

    private static String loadPem(final String valueName, final String pathName) throws Exception {
        final String value = System.getenv(valueName);
        if (StringUtils.isNotBlank(value)) {
            return value.replace("\\n", "\n");
        }

        final String path = System.getenv(pathName);
        if (StringUtils.isBlank(path)) {
            return null;
        }

        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }

    private static PrivateKey readPrivateKey(final String pem) throws Exception {
        final String content = cleanPem(pem, "PRIVATE KEY");
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(content)));
    }

    private static PublicKey readPublicKey(final String pem) throws Exception {
        final String content = cleanPem(pem, "PUBLIC KEY");
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(content)));
    }

    private static String cleanPem(final String pem, final String type) {
        return pem.replace("-----BEGIN " + type + "-----", "")
                .replace("-----END " + type + "-----", "")
                .replaceAll("\\s", "");
    }

    private static String toPublicPem(final byte[] encoded) {
        final String base64 = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.UTF_8)).encodeToString(encoded);
        return "-----BEGIN PUBLIC KEY-----\n" + base64 + "\n-----END PUBLIC KEY-----";
    }
}
