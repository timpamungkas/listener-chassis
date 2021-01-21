package id.co.bfi.listenerchassis.broker.message;

import java.util.List;

public class NotificationMessage {

    private String title;

    private String body;

    private String topicName;

    private String viewName;

    private String clickAction;

    private String isAllUser;

    private List<User> listUser;

    public NotificationMessage() {
    }

    public NotificationMessage(String title, String body, String topicName, String viewName, String clickAction,
                               String isAllUser, List<User> listUser) {
        this.title = title;
        this.body = body;
        this.topicName = topicName;
        this.viewName = viewName;
        this.clickAction = clickAction;
        this.isAllUser = isAllUser;
        this.listUser = listUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public String getIsAllUser() {
        return isAllUser;
    }

    public void setIsAllUser(String isAllUser) {
        this.isAllUser = isAllUser;
    }

    public List<User> getListUser() {
        return listUser;
    }

    public void setListUser(List<User> listUser) {
        this.listUser = listUser;
    }

}
