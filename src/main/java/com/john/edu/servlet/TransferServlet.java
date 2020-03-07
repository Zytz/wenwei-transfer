package com.john.edu.servlet;

import com.john.edu.annotation.Autowired;
import com.john.edu.factory.AnnotationActor;
import com.john.edu.pojo.Result;
import com.john.edu.scan.ScanFile;
import com.john.edu.service.TransferService;
import com.john.edu.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 应癫
 */
@WebServlet(name="transferServlet",urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {

    // 1. 实例化service层对象
    //private TransferService transferService = new TransferServiceImpl();
    //private TransferService transferService = (TransferService) BeanFactory.getBean("transferService");

    // 从工厂获取委托对象（委托对象是增强了事务控制的功能）

    // 首先从BeanFactory获取到proxyFactory代理工厂的实例化对象
//    private ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");

    //    private TransferService transferService = (TransferService) proxyFactory.getJdkProxy(BeanFactory.getBean("transferService")) ;
    @Autowired
    private TransferService transferService;

    @Override
    public void init() {
        scanAllFile();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");

        String fromCardNo = req.getParameter("fromCardNo");
        String toCardNo = req.getParameter("toCardNo");
        String moneyStr = req.getParameter("money");
        int money = Integer.parseInt(moneyStr);

        Result result = new Result();

        try {

            // 2. 调用service层方法
            transferService.transfer(fromCardNo, toCardNo, money);
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("201");
            result.setMessage(e.toString());
        }

        // 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JsonUtils.object2Json(result));
    }

    private static Map<String, Object> map = new HashMap<>();  // 存储对象

    public static Map<String, Object> getMap() {
        return map;
    }


    public void scanAllFile() {
        String[] strings = {"com.john.edu.service", "com.john.edu.dao"};
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
