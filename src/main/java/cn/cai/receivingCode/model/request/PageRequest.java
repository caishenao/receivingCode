package cn.cai.receivingCode.model.request;

import lombok.Data;

/**
 * 分页请求
 */
@Data
public class PageRequest {

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 页数
     */
    private Long pageNumber = 1L;

}
