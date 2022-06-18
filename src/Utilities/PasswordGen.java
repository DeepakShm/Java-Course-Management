package Utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

public class PasswordGen {

    // add exceptions
    public static String passwordEncoder(String password){
        Encoder encoder = Base64.getEncoder();
        return  encoder.encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    // add exceptions
    public static boolean passwordDecoder(String password,String encodedPassword){
        Decoder decoder = Base64.getDecoder();
        byte[] bytes =  decoder.decode(encodedPassword);

        return password.equals(new String(bytes));
    }

}
