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
import org.mindrot.jbcrypt.BCrypt;

/**
 * Password utilities.
 */
public final class Passwords {

    private static final int MIN_PLAIN_PASSWORD_LENGTH = 1;
    private static final int MAX_PLAIN_PASSWORD_LENGTH = 20;
    private static final String DUMMY_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOHIhB7YMqf3ZhqZqBTfQpD7fN1VnV6/u";

    private Passwords() {
    }

    public static String hash(final String plainPassword) {
        if (invalidPlainPassword(plainPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verify(final String plainPassword, final String storedHash) {
        if (StringUtils.isBlank(plainPassword) || StringUtils.isBlank(storedHash) || !storedHash.startsWith("$2")) {
            BCrypt.checkpw(StringUtils.defaultString(plainPassword), DUMMY_HASH);
            return false;
        }

        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (final RuntimeException e) {
            BCrypt.checkpw(StringUtils.defaultString(plainPassword), DUMMY_HASH);
            return false;
        }
    }

    public static boolean invalidPlainPassword(final String password) {
        return null == password || password.length() < MIN_PLAIN_PASSWORD_LENGTH || password.length() > MAX_PLAIN_PASSWORD_LENGTH;
    }
}
