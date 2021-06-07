package cn.icodening.console.server.common.util;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author icodening
 * @date 2021.02.13
 */
public class PageResult<T> {

    private int currentPage;

    private int pageSize;

    private long totalCount;

    private List<T> list;

    private long totalPage;

    private int prev;

    private int next;

    private PageResult(List<T> list) {
        this.list = list;
    }

    public static <T> PageResult<T> createPageResult(List<T> list) {
        return createPageResult(1, Integer.MAX_VALUE, list.size(), list);
    }

    public static <T> PageResult<T> createPageResult(int currentPage, int pageSize, long totalCount, List<T> list) {
        PageResult<T> result = new PageResult<>(list);
        result.setCurrentPage(currentPage);
        result.setPageSize(pageSize);
        result.setTotalCount(totalCount);
        result.setTotalPage(totalCount % pageSize == 0 ? 1 : totalCount / pageSize + 1);
        result.setPrev(currentPage > 1 ? currentPage - 1 : 1);
        result.setNext(currentPage < result.getTotalPage() ? currentPage + 1 : (int) result.getTotalPage());
        return result;
    }

    public static <T> PageResult<T> createPageResult(Page<T> page) {
        int currentPage = page.getPageable().getPageNumber() + 1;
        int pageSize = page.getPageable().getPageSize();
        long totalCount = page.getTotalElements();
        return createPageResult(currentPage, pageSize, totalCount, page.getContent());
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
