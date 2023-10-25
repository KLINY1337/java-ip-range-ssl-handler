package handler;

public class RequestBody {
    private String ipRange;
    private int threadsAmount;

    public String getIpRange() {
        return ipRange;
    }

    public void setIpRange(String ipRange) {
        this.ipRange = ipRange;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public void setThreadsAmount(int threadsAmount) {
        this.threadsAmount = threadsAmount;
    }
}
