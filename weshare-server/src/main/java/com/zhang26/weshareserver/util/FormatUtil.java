package com.zhang26.weshareserver.util;

import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class FormatUtil {

    public static byte[] MultipartFileToBytes(MultipartFile file) throws IOException {
        if (file == null) return null;
        else return file.getBytes();
    }
    public Blob MultipartFileToBlob(MultipartFile file) {
        if (file == null) return null;
        Blob ret = null;
        try {
            byte[] imgBytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
            ret = new SerialBlob(imgBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SerialException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public static String convertBlobToBase64String(Blob blob) {
        String result = "";
        if (null != blob) {
            try {
                InputStream msgContent = blob.getBinaryStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[100];
                int n = 0;
                while (-1 != (n = msgContent.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                result = String.valueOf(Base64.getEncoder().encode(output.toByteArray()));
                output.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return null;
        }
    }

    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
