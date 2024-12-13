package vttp.batch5.ssf.noticeboard.models;

public class HealthStatus {
    private String status;
    private Long timestamp;

    public HealthStatus(String status) {
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
