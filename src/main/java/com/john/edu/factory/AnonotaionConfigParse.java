package com.john.edu.factory;

import com.john.edu.scan.ScanFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:wenwei
 * @date:2020/03/07
 * @description:
 */

public class AnonotaionConfigParse {


    private static Map<String, Object> map = new HashMap<>();  // 存储对象

    public static Map<String, Object> getMap() {
        return map;
    }

    public static void setMap(Map<String, Object> map) {
        AnonotaionConfigParse.map = map;
    }
    public void scanAllFile() {
        String [] strings = {"com.john.edu.service","com.john.edu.dao"};
        for (int i = 0; i < strings.length; i++) {

            ScanFile scanFile = new ScanFile();
            scanFile.addClass(strings[i]);

            Map<String, Class> mapClass = scanFile.map;
            for (Map.Entry<String, Class> entry : mapClass.entrySet()) {
                AnnotationActor annotationActor = new AnnotationActor();
                annotationActor.isConponentAnnotion(entry.getValue());
                map.putAll(annotationActor.map);
            }
        }


    }
}
