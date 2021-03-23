// package com.wen.eureka.cloud.spring.config;
//
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
// /**
//  * 项目名称：fint_iia_phosphor_server
//  * 类名称：WebSecurityConfig
//  * 类描述：EurekaServer注册中心需要关闭 /eureka 路径的SpringSecurity的csrf验证,不然客户端连接不上注册中心
//  * 创建人：yingx
//  * 创建时间：2020/9/22
//  * 修改人：yingx
//  * 修改时间：2020/9/22
//  * 修改备注：
//  */
// @EnableWebSecurity
// public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         http.csrf().ignoringAntMatchers("/eureka/**");
//         super.configure(http);
//     }
// }
