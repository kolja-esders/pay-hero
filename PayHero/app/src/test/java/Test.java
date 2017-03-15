/**
 * Created by david on 13.03.17.
 */

import org.apache.commons.codec.binary.Base64;

public class Test {

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();

        // composing string to be encoded
        sb.append("Part 1 of some text to be encoded to base64 format\n");
        sb.append("Part 2 of some text to be encoded to base64 format\n");
        sb.append("Part 3 of some text to be encoded to base64 format");


        // getting base64 encoded string bytes
        byte[] bytesEncoded =  Base64.encodeBase64(sb.toString().getBytes());

        // composing json
        String json = "{\"serialDataByte\":\""+ new String(bytesEncoded) +"\"}";

        System.out.println(json);

    }
}
