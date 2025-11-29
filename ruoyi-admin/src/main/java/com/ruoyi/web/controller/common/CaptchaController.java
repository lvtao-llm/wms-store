package com.ruoyi.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 验证码操作处理
 * 
 * @author ruoyi
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ISysConfigService configService;
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = RuoYiConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {

            String capText = captchaProducerMath.createText();
            capText = createMathCaptchaText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    /**
     * 生成结果为1-99范围内且结果为整数的数学公式验证码
     * 结果限定为包含1到5的一或两位数字
     * @return 数学公式文本和结果，格式为"公式@结果"
     */
    private String createMathCaptchaText() {
        // 定义运算符
        String[] operators = {"+", "-", "*"};
        Random random = new Random();

        // 生成第一个操作数 (1-20)
        int operand1 = random.nextInt(20) + 1;

        // 随机选择运算符
        String operator = operators[random.nextInt(operators.length)];

        int operand2, result;
        do {
            // 根据运算符生成第二个操作数和计算结果
            switch (operator) {
                case "+":
                    // 加法：确保结果在1-5的一或两位数范围内
                    operand2 = random.nextInt(50) + 1;
                    result = operand1 + operand2;
                    break;
                case "-":
                    // 减法：确保结果为正且在指定范围内
                    operand2 = random.nextInt(operand1) + 1;
                    result = operand1 - operand2;
                    break;
                case "*":
                    // 乘法：确保结果在指定范围内
                    operand2 = random.nextInt(8) + 1;
                    result = operand1 * operand2;
                    break;
                default:
                    operand2 = 1;
                    result = operand1;
            }
        } while (result < 1 || result > 55 ||
                (result > 9 && !(result >= 10 && result <= 15 || result >= 20 && result <= 25 ||
                        result >= 30 && result <= 35 || result >= 40 && result <= 45 ||
                        result >= 50 && result <= 55)));

        // 构造公式文本
        String formula = operand1 + operator + operand2;
        return formula + "@" + result;
    }
}
