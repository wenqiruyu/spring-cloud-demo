package com.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：spring-cloud-demo
 * 类名称：UserController
 * 类描述：用户模块Controller
 * 创建人：yingx
 * 创建时间： 2021/3/18
 * 修改人：yingx
 * 修改时间： 2021/3/18
 * 修改备注：
 */
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
      * 通过用户名称获取用户信息
      *
      * @param name 用户名字
      * @return java.lang.String
      * @author yingx
      * @date 2021/3/18
     **/
    @GetMapping("/usr/{name}")
    public String getUser(@PathVariable String name){

        logger.info("<用户模块>-通过用户姓名获取用户信息,用户名为 --- {}", name);

        return "&lt;user-server&gt; - " + name + "，你好";
    }
}
