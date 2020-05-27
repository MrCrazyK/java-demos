package com.k.es.util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/4/22 10:13
 **/
public class IndexColumn {
    @NotNull(
            message = "索引字段名称不能为空"
    )
    @NotBlank(
            message = "索引字段名称不能为空"
    )
    private String columnName;
    private String columnDesc;
    @NotNull(
            message = "索引字段类型不能为空"
    )
    @NotBlank(
            message = "索引字段类型不能为空"
    )
    private String columnType = null;
    @NotNull(
            message = "索引字段是否存储不能为空"
    )
    @NotBlank(
            message = "索引字段是否存储不能为空"
    )
    private Boolean stored = true;
    @NotNull(
            message = "索引字段是否创建索引不能为空"
    )
    @NotBlank(
            message = "索引字段是否创建索引不能为空"
    )
    private Boolean indexed = true;
    private String defaultValue = null;
    private String format = null;
    @NotNull(
            message = "索引字段是否默认字段不能为空"
    )
    @NotBlank(
            message = "索引字段是否默认字段不能为空"
    )
    private Boolean defColumn = false;
    private Map<String, Object> config = null;
    private String copyTo = null;
    private boolean stop = false;
    private boolean splitNumAndEn = false;
    /**
     * @deprecated
     */
    @Deprecated
    private Boolean isPk = false;
    private Map<String, Object> params = null;

    /**
     * @deprecated
     */
    @Deprecated
    public IndexColumn() {
    }

    public IndexColumn(String var1, String var2, IndexColumnType var3) {
        this.columnName = var1;
        this.columnType = var3.toString();
        this.columnDesc = var2;
    }

    public IndexColumn(String var1, String var2, IndexColumnType var3, Boolean var4, Boolean var5) {
        this.columnName = var1;
        this.columnType = var3.toString();
        this.columnDesc = var2;
        this.indexed = var4;
        this.stored = var5;
    }

    public IndexColumnType getColumnTypeEnum() {
        return this.columnType != null ? (IndexColumnType) Enum.valueOf(IndexColumnType.class, this.columnType) : null;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public IndexColumn setColumnName(String var1) {
        this.columnName = var1;
        return this;
    }

    public String getColumnDesc() {
        return this.columnDesc;
    }

    public IndexColumn setColumnDesc(String var1) {
        this.columnDesc = var1;
        return this;
    }

    public String getColumnType() {
        return this.columnType;
    }

    public IndexColumn setColumnType(String var1) {
        this.columnType = var1;
        return this;
    }

    public Boolean getStored() {
        return this.stored;
    }

    public IndexColumn setStored(Boolean var1) {
        this.stored = var1;
        return this;
    }

    public Boolean getIndexed() {
        return this.indexed;
    }

    public IndexColumn setIndexed(Boolean var1) {
        this.indexed = var1;
        return this;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public IndexColumn setDefaultValue(String var1) {
        this.defaultValue = var1;
        return this;
    }

    public Boolean getDefColumn() {
        return this.defColumn;
    }

    public IndexColumn setDefColumn(Boolean var1) {
        this.defColumn = var1;
        return this;
    }

    public Map<String, Object> getConfig() {
        return this.config;
    }

    public IndexColumn setConfig(Map<String, Object> var1) {
        this.config = var1;
        return this;
    }

    public String getCopyTo() {
        return this.copyTo;
    }

    public IndexColumn setCopyTo(String var1) {
        this.copyTo = var1;
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Boolean getIsPk() {
        return this.isPk;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setIsPk(Boolean var1) {
        this.isPk = var1;
    }

    public String getFormat() {
        return this.format;
    }

    public IndexColumn setFormat(String var1) {
        this.format = var1;
        return this;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public IndexColumn setParams(Map<String, Object> var1) {
        this.params = var1;
        return this;
    }

    public boolean isStop() {
        return this.stop;
    }

    public IndexColumn setStop(boolean var1) {
        this.stop = var1;
        return this;
    }

    public boolean isSplitNumAndEn() {
        return this.splitNumAndEn;
    }

    public IndexColumn setSplitNumAndEn(boolean var1) {
        this.splitNumAndEn = var1;
        return this;
    }
}
