package com.ruoyi.framework.web.service;

import com.ruoyi.framework.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;




    /**
     * 邮箱验证
     */
    public String registerSendEmail(RegisterBody registerBody){
        String msg = "", username = registerBody.getUsername();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        if (StringUtils.isEmpty(username))
        {
            msg = "用户邮箱不能为空";
        }else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "邮箱验证'" + username + "'失败，注册邮箱账号已存在";
        }else if(!isValidEmail(username)){
            msg = "'" + username + "'邮箱格式不正确";
        }else {
            //生成六位数验证码
            String code = generateVerificationCode();
            //key
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + username;
            // 设置五分钟失效
            redisCache.setCacheObject(verifyKey, code, 5, TimeUnit.MINUTES);
            // 发送邮箱
            EmailUtils.sendSimpleEmail(username,"你的验证码为 "+ code,"你的邮箱验证码为 "+ code);
        }

        return msg;
    }


    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

//        // 验证码开关
//        boolean captchaEnabled = configService.selectCaptchaEnabled();
//        if (captchaEnabled)
//        {
//            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
//        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户邮箱不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册邮箱账号已存在";
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    public  String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // 生成0到9之间的随机数
        }

        return code.toString();
    }

    /**
     * 邮箱格式校验
     * @param email
     * @return
     */
    public  boolean isValidEmail(String email) {
        final String EMAIL_PATTERN =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @return 结果
     */
    public String validateCode(String username, String code)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + username;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
        return "";
    }
}
