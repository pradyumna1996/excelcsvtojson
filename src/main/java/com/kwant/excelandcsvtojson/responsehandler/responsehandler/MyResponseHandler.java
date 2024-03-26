package com.kwant.excelandcsvtojson.responsehandler.responsehandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;


public class MyResponseHandler {
    public static ResponseEntity<Object> generateResponse(Boolean success, HttpStatus status, String name,String type,double size, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", success);
        map.put("name", name);
        map.put("type", type);
        map.put("size", size);

        map.put("records", responseObj);

        return new ResponseEntity<Object>(map, status);
    }

}
