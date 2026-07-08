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

import org.testng.Assert;
import org.testng.annotations.Test;

public class PasswordsTest {

    @Test
    public void hashAndVerify() {
        final String hash1 = Passwords.hash("password");
        final String hash2 = Passwords.hash("password");

        Assert.assertTrue(hash1.startsWith("$2"));
        Assert.assertNotEquals(hash1, hash2);
        Assert.assertTrue(Passwords.verify("password", hash1));
        Assert.assertFalse(Passwords.verify("wrong", hash1));
    }

    @Test
    public void rejectsMd5AndInvalidValues() {
        Assert.assertFalse(Passwords.verify("password", "5f4dcc3b5aa765d61d8327deb882cf99"));
        Assert.assertFalse(Passwords.verify("password", "not-bcrypt"));
        Assert.assertFalse(Passwords.verify("password", ""));
        Assert.assertFalse(Passwords.verify("", Passwords.hash("password")));
    }

    @Test
    public void validatesPlainPasswordLength() {
        Assert.assertTrue(Passwords.invalidPlainPassword(null));
        Assert.assertTrue(Passwords.invalidPlainPassword(""));
        Assert.assertFalse(Passwords.invalidPlainPassword("123456"));
        Assert.assertFalse(Passwords.invalidPlainPassword("12345678901234567890"));
        Assert.assertTrue(Passwords.invalidPlainPassword("123456789012345678901"));
    }
}
