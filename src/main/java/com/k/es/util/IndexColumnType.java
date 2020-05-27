package com.k.es.util;

public enum IndexColumnType {
    VALUE,
    STRING,
    TEXT_AND_STRING,
    ANALYSIS,
    ANALYSIS_AND_STRING,
    ANALYSIS_WHITESPACE,
    ANALYSIS_WHITESPACE_AND_STRING,
    DATE,
    DATETIME,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    TEXT,
    ENUM,
    /**
     * @deprecated
     */
    @Deprecated
    KEYWORD,
    /**
     * @deprecated
     */
    @Deprecated
    INTEGER,
    NULL;

    private IndexColumnType() {
    }
}
