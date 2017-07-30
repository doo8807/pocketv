package com.example.doo88.pocketv;

/**
 * Created by doo88 on 2017-02-18.
 */

public class dbConnect {



    String returnvalue, data;

    public String json(String... params) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.length; i++) {

            String startJson = "[";
            String endJson = "]";

            if (!sb.toString().equals("")) {
                sb.append(",");
            }
            String temp = "{\"data\"" + ":" + "\"" + params[i] + "\"}";
            sb.append(temp);
            returnvalue = startJson + sb + endJson;
        }

        return returnvalue;
    }



}
