package cn.addenda.seckilldemo.filter;

import cn.addenda.seckilldemo.util.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author addenda
 * @datetime 2022/12/10 17:41
 */
public class UserFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userid = request.getHeader("userid");
        if (StringUtils.isBlank(userid)) {
            response.setStatus(401);
            return false;
        }
        UserHolder.saveUser(userid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUserId();
    }
}
