package com.k.es.util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/4/22 10:16
 **/
public class IndexTable {
    @NotNull(
            message = "索引表名称不能为空"
    )
    @NotBlank(
            message = "索引表名称不能为空"
    )
    private String tableName;
    private Integer shards = null;
    private Integer replicas = null;
    private String alias;
    private List<IndexColumn> columns = null;
    private IndexColumn pkColumn = null;
    private int refreshInterval = 10;

    /**
     * @deprecated
     */
    @Deprecated
    public IndexTable() {
    }

    public IndexTable(String var1) {
        this.tableName = var1;
    }

    public IndexTable(String var1, Integer var2, Integer var3) {
        this.tableName = var1;
        this.shards = var2;
        this.replicas = var3;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<IndexColumn> getColumns() {
        return this.columns;
    }

    public void setColumns(List<IndexColumn> var1) {
        this.columns = var1;
    }

    public void addColumn(IndexColumn var1) {
        if (var1 != null) {
            if (this.columns == null) {
                this.columns = new ArrayList<>();
            }

            this.columns.add(var1);
        }

    }

    public IndexColumn getPkColumn() {
        return this.pkColumn;
    }

    public void setPkColumn(IndexColumn var1) {
        this.pkColumn = var1;
    }

    public Integer getShards() {
        return this.shards;
    }

    public void setShards(Integer var1) {
        this.shards = var1;
    }

    public Integer getReplicas() {
        return this.replicas;
    }

    public void setReplicas(Integer var1) {
        this.replicas = var1;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String var1) {
        this.alias = var1;
    }

    public int getRefreshInterval() {
        return this.refreshInterval;
    }

    public void setRefreshInterval(int var1) {
        this.refreshInterval = var1;
    }
}
