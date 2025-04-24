package hu.waiter.blsz.model;

public class MessageRequest {
    private String originalText;
    private boolean openingHours;
    private boolean haccpInfo;

    public String getOriginalText() {
        return originalText;
    }
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }
    public boolean isOpeningHours() {
        return openingHours;
    }
    public void setOpeningHours(boolean openingHours) {
        this.openingHours = openingHours;
    }
    public boolean isHaccpInfo() {
        return haccpInfo;
    }
    public void setHaccpInfo(boolean haccpInfo) {
        this.haccpInfo = haccpInfo;
    }
}
