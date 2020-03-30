package com.yq.news.itf;

/**
 * 网格首页数据需要实现的接口
 */
public interface IGridItem {
    /**
     * 是否启用分割线
     * @return true
     */
    boolean isShow();

    /**
     * 分类标签
     */
    String getTag();

    /**
     * 权重
     */
    int getSpanSize();
}
