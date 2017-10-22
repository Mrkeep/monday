package com.hyb.backstage.utils;

import com.hyb.common.dal.entity.backstage.Admin;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class AuthenticatorUtils {
    /**
     * 通用生成密码散列值方法，hashIterations表示散列次数，例如md5(md5(password + salt))，hashIterations=2
     *
     * @param algorithmName
     * @param password
     * @param salt
     * @param hashIterations
     * @return
     */
    public static Hash generateHashValue(String algorithmName, String password, String salt, int hashIterations) {
        SimpleHash hash = new SimpleHash(algorithmName, password, salt, hashIterations);
        return hash;
    }

    /**
     * 生成md5散列值
     *
     * @param password
     * @param salt
     * @return
     */
    public static String generateMD5HashValue(String password, String salt) {
        Hash hash = AuthenticatorUtils.generateHashValue("MD5", password, salt, 1);
        return hash.toHex();//toString == toHex；there is anther method: toBase64
    }

    /**
     * 生成随机私盐
     *
     * @return
     */
    public static String generateRandomSaltValue() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }

    public static void encryptPassword(Admin user) {
        String randomSalt = AuthenticatorUtils.generateRandomSaltValue();
        user.setSalt(randomSalt);

        String passwordHashValue = AuthenticatorUtils.generateHashValue("md5", user.getPassword(), getSalt(user), 1).toString();//toString == toHex；there is anther method: toBase64
        user.setPassword(passwordHashValue);
    }

    public static boolean validPassword(Admin user, String password) {
        String passwordHashValue = AuthenticatorUtils.generateHashValue("md5", password, getSalt(user), 1).toString();
        return user.getPassword().equals(passwordHashValue);
    }

    public static String getSalt(Admin user) {
        return user.getUsername() + user.getSalt();
    }
}
