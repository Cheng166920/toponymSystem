package com.example.toponym.model;
import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Data
@Schema(name = "PageBean对象",description = "分页信息实体")
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(name = "pageNum",description = "当前页")
    private int pageNum;
    @Schema(name = "pageSize",description = "每页的数量")
    private int pageSize;
    @Schema(name = "total",description = "总记录数")
    private long total;
    @Schema(name = "pages",description = "总页数")
    private int pages;
    @Schema(name = "data",description = "结果集")
    private List<T> data;

    public PageBean(List<T> data) {
        this(data, 8);
    }

    public PageBean(List<T> data, int navigatePages) {
        if (data instanceof Page) {
            Page page = (Page) data;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.pages = page.getPages();
            this.data = page;
            this.total = page.getTotal();
        } else if (data instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = data.size();
            this.pages = this.pageSize > 0 ? 1 : 0;
            this.data = data;
            this.total = data.size();
        }

    }
}
