package com.tyj.community.controller.admin;

import com.tyj.community.constant.WebConstant;
import com.tyj.community.controller.BaseController;
import com.tyj.community.dto.LogActions;
import com.tyj.community.entity.RestResponseBo;
import com.tyj.community.entity.UserVo;
import com.tyj.community.exception.TipException;
import com.tyj.community.service.LogService;
import com.tyj.community.service.UserService;
import com.tyj.community.utils.Commons;
import com.tyj.community.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by tyj on 2018/11/27.
 */
@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class AuthController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService usersService;

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/login")
    public String login() {
        System.out.println("后台登录");
        return "admin/login";
    }

    /**
     * 管理后台登录
     * @param username
     * @param password
     * @param remeber_me
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "login")
    @ResponseBody
    public RestResponseBo doLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String remeber_me,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        Integer error_count = cache.get("login_error_count");
        try {
            UserVo user = usersService.login(username, password);
            request.getSession().setAttribute(WebConstant.LOGIN_SESSION_KEY, user);
            if (StringUtils.isNotBlank(remeber_me)) {
                TaleUtils.setCookie(response, user.getId());
            }
            logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), user.getId());
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return RestResponseBo.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "登录失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    /**
     * 注销
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConstant.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConstant.USER_IN_COOKIE, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        try {
            //response.sendRedirect(Commons.site_url());
            response.sendRedirect(Commons.site_login());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
