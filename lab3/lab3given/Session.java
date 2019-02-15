import java.util.*;

/**
 * Created by Justin Nguyen on 10/11/2017.
 */
public class Session {

    private String sessionId;
    private String customerId;
    private final List<Object> actions               = new LinkedList<>();
    private final List<ViewAction> viewActions = new LinkedList<>();
    private final List<BuyAction> buyActions   = new LinkedList<>();


    public Session(String sessionId, String customerId) {
        this.sessionId = sessionId;
        this.customerId = customerId;
    }

    public void add(BuyAction buyAction) {
        actions.add(buyAction);
        buyActions.add(buyAction);
    }

    public void add(ViewAction viewAction) {
        actions.add(viewAction);
        viewActions.add(viewAction);
    }

    public void add(EndAction endAction) {
        actions.add(endAction);
    }

    public Map<String, List<ViewAction>> getViewsFromSession() {
        Map<String, List<ViewAction>> viewsFromSession = new HashMap<>();
        viewsFromSession.put(sessionId, viewActions);
        return viewsFromSession;
    }

    public Map<String, List<BuyAction>> getBuysFromSession() {
        Map<String, List<BuyAction>> buysFromSession = new HashMap<>();
        buysFromSession.put(sessionId, buyActions);
        return buysFromSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCustomerId() {
        return customerId;
    }
    
    public List<Object> getActions() {
        return actions;
    }
    
    public List<ViewAction> getViewActions() {
        return viewActions;
    }
    
    public List<BuyAction> getBuyActions() {
        return buyActions;
    }


}
