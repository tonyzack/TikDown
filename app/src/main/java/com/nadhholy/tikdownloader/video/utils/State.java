package com.nadhholy.tikdownloader.video.utils;

import android.text.TextUtils;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class State {

    public static String a = "AES";
    public static String z = "AES/CBC/PKCS5PADDING";

    public static String getState(String plainText)
    {
        try {

            if (TextUtils.isEmpty(plainText))
                return null;

            byte[] plainTextArray = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] keyArray = DatatypeConverter.parseBase64Binary(Y.ky);
            byte[] iv = DatatypeConverter.parseBase64Binary(Y.iv);

            SecretKeySpec secretKey = new SecretKeySpec(keyArray, a);
            Cipher cipher = Cipher.getInstance(z);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            return DatatypeConverter.printBase64Binary(cipher.doFinal(plainTextArray));

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
