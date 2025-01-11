package com.caixy.adminSystem.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * 数据库处理器
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/10 2:15
 */
@Configuration
public class DataObjectHandler implements MetaObjectHandler
{

    @Override
    public void insertFill(MetaObject metaObject)
    {
        // 插入时自动填充
        this.setFieldValByNameIfNull("createTime", new Date(), metaObject);
        this.setFieldValByNameIfNull("updateTime",  new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject)
    {
        // 更新时自动填充
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    private void setFieldValByNameIfNull(String fieldName, Object fieldVal, MetaObject metaObject)
    {
        if (metaObject.getValue(fieldName) == null)
        {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
        }
    }
}
