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
var SymCrypto = {
  publicKey: '',
  getPublicKey: function (callback) {
    if (SymCrypto.publicKey) {
      callback(SymCrypto.publicKey)
      return
    }

    $.ajax({
      url: Label.servePath + '/crypto/rsa/public-key',
      type: 'GET',
      cache: false,
      success: function (result) {
        if (0 === result.code && result.publicKey) {
          SymCrypto.publicKey = result.publicKey
          callback(SymCrypto.publicKey)
        }
      },
    })
  },
  encryptPassword: function (password, callback) {
    SymCrypto.getPublicKey(function (publicKey) {
      var crypt = new JSEncrypt()
      crypt.setPublicKey(publicKey)
      callback(crypt.encrypt(password))
    })
  },
}
