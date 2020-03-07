package com.john.edu.factory;

import com.john.edu.utils.ConnectionUtils;

/**
 * @author 应癫
 */
public class CreateBeanFactory {



    public static ConnectionUtils getInstanceStatic() {
        return new ConnectionUtils();
    }


    public ConnectionUtils getInstance() {
        return new ConnectionUtils();
    }
}
