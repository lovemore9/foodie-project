package com.imooc.controller.test;

import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:58
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisController {

    private final RedisOperator redisOperator;

    @GetMapping("/set")
    public IMOOCJSONResult set(String key, String value) {
        redisOperator.set(key, value);
        return IMOOCJSONResult.ok();
    }

    @GetMapping("/get")
    public IMOOCJSONResult get(String key) {
        String s = redisOperator.get(key);
        return IMOOCJSONResult.ok(s);
    }

    @GetMapping("/delete")
    public IMOOCJSONResult delete(String key) {
        Boolean del = redisOperator.del(key);
        return IMOOCJSONResult.ok(del);
    }


}
