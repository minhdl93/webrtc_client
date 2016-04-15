package fr.pchab.androidrtc.Model;

/**
 * Created by gumiMinh on 4/11/16.
 */
public class HistoryItem {
    private String userId;
    private String status;
    private String userName;

    public HistoryItem(String userId,String userName) {
        this.userId = userId;
        this.userName = userName;
        this.status = "Offline";
    }

    public HistoryItem(String userId,String userName, String status) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (!(o instanceof HistoryItem)) return false;
        HistoryItem cu = (HistoryItem)o;
        return this.userId.equals(((HistoryItem) o).getUserId());
    }

    @Override
    public int hashCode() {
        return this.getUserId().hashCode();
    }
}