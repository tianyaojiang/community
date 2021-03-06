package com.tyj.community.controller;

import com.tyj.community.entity.UserVo;
import com.tyj.community.utils.MapCache;
import com.tyj.community.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tyj on 2018/11/27.
 */
public class BaseController {
    public static String THEME = "themes";

    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }






    public Integer getUid(HttpServletRequest request){
        return this.user(request).getId();
    }

    public String render_404() {
        return "commons/error";
    }
}
