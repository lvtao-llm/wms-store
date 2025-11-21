package com.ruoyi.framework.datasource;

import java.util.Map;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 动态数据源
 *
 * @author ruoyi
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Nullable
    private Map<Object, Object> targetDataSources;

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    public Map<Object, Object> getTargetDataSources() {
        return this.targetDataSources;
    }
}