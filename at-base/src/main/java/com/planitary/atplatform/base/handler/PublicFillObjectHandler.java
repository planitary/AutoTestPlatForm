package com.planitary.atplatform.base.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 公共字段填充
 */
@Component
@Slf4j
public class PublicFillObjectHandler implements MetaObjectHandler {

    // 格式化日期
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充，事件:insert");
        metaObject.setValue("createTime", LocalDateTime.now().format(formatter));
        metaObject.setValue("updateTime", LocalDateTime.now().format(formatter));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充，事件:update");
        metaObject.setValue("updateTime", LocalDateTime.now().format(formatter));

    }
}
