package cn.icodening.console.ratelimit.cache;

import cn.icodening.console.common.entity.RateLimitEntity;

import java.util.concurrent.Semaphore;

/**
 * @author icodening
 * @date 2021.06.09
 */
public class RateLimitCache {

    private RateLimitEntity rateLimitEntity;

    private String endpoint;

    private volatile Integer frequency;

    private String dimension;

    private volatile Boolean enable;

    private volatile long startTime;

    private volatile long endTime;

    private volatile long addTime;

    private volatile Semaphore semaphore;

    public RateLimitCache(RateLimitEntity rateLimitEntity) {
        this.rateLimitEntity = rateLimitEntity;
        this.dimension = rateLimitEntity.getDimension();
        this.enable = rateLimitEntity.getEnable();
        this.frequency = rateLimitEntity.getFrequency();
        this.endpoint = rateLimitEntity.getEndpoint();
    }

    public RateLimitEntity getRateLimitEntity() {
        return rateLimitEntity;
    }

    public void setRateLimitEntity(RateLimitEntity rateLimitEntity) {
        this.rateLimitEntity = rateLimitEntity;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public synchronized void refresh() {
        this.semaphore = new Semaphore(frequency);
        long now = System.currentTimeMillis();
        this.startTime = now;
        this.endTime = now + addTime;
    }

    @Override
    public String toString() {
        return "RateLimitCache{" +
                "endpoint='" + endpoint + '\'' +
                ", frequency=" + frequency +
                ", dimension='" + dimension + '\'' +
                ", enable=" + enable +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", addTime=" + addTime +
                ", semaphore=" + semaphore +
                '}';
    }
}
