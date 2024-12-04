package com.andrew.mianshidog.satoken;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.andrew.mianshidog.common.ErrorCode;
import com.andrew.mianshidog.exception.ThrowUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录设备信息类
 */
public class DeviceUtils {

    /**
     * 根据请求获取设备信息
     *
     * @param request
     * @return
     */
    public static String getRequestDevice(HttpServletRequest request) {
        String userAgentStr = request.getHeader(Header.USER_AGENT.toString());
        // 使用hutool解析UserAgent
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        ThrowUtils.throwIf(userAgent == null, ErrorCode.OPERATION_ERROR, "非法请求");
        // 默认设备是pc
        String device = "pc";
        if (isMiniProgram(userAgentStr)) {
            device = "miniProgram";
        } else if (isPad(userAgentStr)) {
            device = "pad";
        } else if (userAgent.isMobile()) {
            device = "mobile";
        }
        return device;
    }

    private static boolean isMiniProgram(String userAgentStr) {
        return StrUtil.containsIgnoreCase(userAgentStr, "MicroMessenger")
                && StrUtil.containsIgnoreCase(userAgentStr, "MiniProgram");
    }

    private static boolean isPad(String userAgentStr) {
        // 检查iPad的User-Agent标志
        boolean isIpad = StrUtil.containsIgnoreCase(userAgentStr, "iPad");

        // 检查Android平板
        boolean isAndroidPad = StrUtil.containsIgnoreCase(userAgentStr, "Android")
                && !StrUtil.containsIgnoreCase(userAgentStr, "Mobile");

        // 如果是iPad或android平板返回true
        return isIpad || isAndroidPad;
    }
}
