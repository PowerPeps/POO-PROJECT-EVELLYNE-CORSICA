
public class Choice {
    private String text;
    private String targetSceneId;
    private String condition;

    public Choice(String text, String targetSceneId, String condition) {
        this.text = text;
        this.targetSceneId = targetSceneId;
        this.condition = condition;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTargetSceneId() { return targetSceneId; }
    public void setTargetSceneId(String targetSceneId) { this.targetSceneId = targetSceneId; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
}
