package lk.ijse.therapycenter.util;

import java.util.UUID;

public class IdGeneratorUtil {

    public static String generateNextId(String prefix, String lastId) {


        if (lastId == null) {
            return prefix + "001";
        }

        String num = lastId.replaceAll("[^0-9]","");

        return String.format("%s%03d", prefix, Integer.parseInt(num)+1);

    }
}
