package org.jeecg.modules.KM.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static String sha256(File file){
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return sha256(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha256(String s) {
        InputStream fis = new ByteArrayInputStream(s.getBytes());
        try {
            return sha256(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha256(InputStream inputStream) throws IOException

    {
        byte buffer[] = new byte[1024 * 10];
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("SHA-256");
            for (int numRead = 0; (numRead = inputStream.read(buffer)) > 0; ) {
                md5.update(buffer, 0, numRead);
            }
            byte[] digest = md5.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    private static String toHexString(byte[] digest) {
        StringBuffer strHexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xff & digest[i]);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }
        return strHexString.toString();
    }
}
