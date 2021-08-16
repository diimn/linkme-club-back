package org.feelfee.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.nio.ByteBuffer;
import java.util.Base64;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class Utils {
    public static String getUniqLink() {

        long val = Math.round(Math.random() * 1_000_000_000);
        System.out.println(val);
        byte[] bytes = intToBytes((int) val);
        String res = new String(Base64.getEncoder().withoutPadding().encode(bytes));
        return res.replace("+", "-").replace("/", "_");

    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static String getUserFromJWT(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String managerName = jwt.getClaim("user").asString();
        System.out.println("MANAGER:" + managerName);
        return managerName;
    }
}
