package de.lars.javacraft.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Resource {
   public static String asString(String path) throws IOException {
       try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {

           ByteArrayOutputStream result = new ByteArrayOutputStream();

           byte[] buffer = new byte[1024];
           for (int length; (length = inputStream.read(buffer)) != -1; ) {
               result.write(buffer, 0, length);
           }

           return result.toString(StandardCharsets.UTF_8);
       }
   }

}
