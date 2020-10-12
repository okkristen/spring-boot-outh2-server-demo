package com.hzys.ssoserver.restResult;


import com.hzys.ssoserver.utils.CoreUtils;

/**
 * @author lirong
 * @ClassName: PageParam
 * @Description: 分页信息的封装
 * @date 2018-11-02 10:28
 */
public class PageParam {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页尺寸
     */
    private Integer size = 10;

    /**
     * 排序字段，默认使用ID来排序
     */
    private String sortField = "id";

    /**
     * 排序方式，默认升序
     */
    private String sortOrder = "DESC";

    public String getOrderBy(){
        return CoreUtils.getOrderBy(sortField, sortOrder);
    }



}
