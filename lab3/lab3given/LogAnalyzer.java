import java.lang.String;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LogAnalyzer {
    //constants to be used when pulling data out of input
    //leave these here and refer to them to pull out values
    private static final String START_TAG = "START";
    private static final int START_NUM_FIELDS = 3;
    private static final int START_SESSION_ID = 1;
    private static final int START_CUSTOMER_ID = 2;
    private static final String BUY_TAG = "BUY";
    private static final int BUY_NUM_FIELDS = 5;
    private static final int BUY_SESSION_ID = 1;
    private static final int BUY_PRODUCT_ID = 2;
    private static final int BUY_PRICE = 3;
    private static final int BUY_QUANTITY = 4;
    private static final String VIEW_TAG = "VIEW";
    private static final int VIEW_NUM_FIELDS = 4;
    private static final int VIEW_SESSION_ID = 1;
    private static final int VIEW_PRODUCT_ID = 2;
    private static final int VIEW_PRICE = 3;
    private static final String END_TAG = "END";
    private static final int END_NUM_FIELDS = 2;
    private static final int END_SESSION_ID = 1;

    private static void processStartEntry(
            final String[] words,
            final Map<String, List<Session>> sessionsFromCustomer,
            final Map<String, Session> sessionsList) {
        if (words.length != START_NUM_FIELDS) {
            return;
        }

        // Check if session exists
        List<Session> customerSessions = sessionsFromCustomer.get(words[START_CUSTOMER_ID]);
        if (customerSessions == null) {
            customerSessions = new LinkedList<>();
            sessionsFromCustomer.put(words[START_CUSTOMER_ID], customerSessions);
        }

        // Add session to customer's list of sessions
        Session session = new Session(words[START_SESSION_ID], words[START_CUSTOMER_ID]);
        customerSessions.add(session);

        // Register session with a master sessions map
        sessionsList.put(words[START_SESSION_ID], session);
    }

    private static void processViewEntry(
            final String[] words,
            final Map<String, Session> sessionsList,
            final Map<String, Product> productsList) {
        if (words.length != VIEW_NUM_FIELDS) {
            return;
        }

        Session session = sessionsList.get(words[VIEW_SESSION_ID]);
        if (session == null) {
            // TODO: Throw error because trying to view item before session start
            // TODO: Check that session has not ended
            return;
        }

        Product product = productsList.get(words[VIEW_PRODUCT_ID]);
        if (product == null) {
            product = new Product(words[VIEW_PRODUCT_ID], Integer.parseInt(words[VIEW_PRICE]));
            productsList.put(words[VIEW_PRODUCT_ID], product);
        }

        ViewAction viewAction = new ViewAction(words[VIEW_SESSION_ID], product);
        session.add(viewAction);
    }

    private static void processBuyEntry(
            final String[] words,
            final Map<String, Session> sessionsList,
            final Map<String, Product> productsList) {
        if (words.length != BUY_NUM_FIELDS) {
            return ;
        }

        Session session = sessionsList.get(words[BUY_SESSION_ID]);
        if (session == null) {
            // TODO: Throw error because trying to buy item before session start
            // TODO: Check that session has not ended
            return;
        }

        Product product = productsList.get(words[BUY_PRODUCT_ID]);
        if (product == null) {
            product = new Product(words[BUY_PRODUCT_ID], Integer.parseInt(words[BUY_PRICE]));
            productsList.put(words[BUY_PRODUCT_ID], product);
        }

        BuyAction buyAction = new BuyAction(words[BUY_SESSION_ID], product, Integer.parseInt(words[BUY_QUANTITY]));
        session.add(buyAction);
    }

    private static void processEndEntry(
            final String[] words,
            final Map<String, Session> sessionsList) {
        if (words.length != END_NUM_FIELDS) {
            return;
        }

        Session session = sessionsList.get(words[END_SESSION_ID]);
        if (session == null) {
            // TODO: Throw error because trying to end session before session start
            return;
        }

        EndAction endAction = new EndAction(words[END_SESSION_ID]);
        session.add(endAction);
    }

    private static void processLine(
            final String line,
            final Map<String, List<Session>> sessionsFromCustomer,
            final Map<String, Session> sessionsList,
            final Map<String, Product> productsList) {
        final String[] words = line.split("\\h");

        if (words.length == 0) {
            return;
        }

        switch (words[0]) {
            case START_TAG:
                processStartEntry(words, sessionsFromCustomer, sessionsList);
                break;
            case VIEW_TAG:
                processViewEntry(words, sessionsList, productsList);
                break;
            case BUY_TAG:
                processBuyEntry(words, sessionsList, productsList);
                break;
            case END_TAG:
                processEndEntry(words, sessionsList);
                break;
        }
    }

    private static void averageViewsWOPurchase(Map<String, Session> sessionsList) {
        /*
         * Prints the average number of products viewed for sessions where the customer did NOT buy anything.
         * NOTE: This is number of view actions not number of items viewed!
         */
        System.out.print("Average Views without Purchase: ");
        int viewActionsCount = 0;
        int customerCount = 0;
        for (Map.Entry<String, Session> session: sessionsList.entrySet()) {
            Session theSession = session.getValue();
            if (theSession.getBuyActions().size() == 0) {
                viewActionsCount += theSession.getViewActions().size();
                customerCount += 1;
            }
        }
        System.out.printf("%.1f", (double)viewActionsCount / customerCount);
        System.out.println();
    }

    private static void printSessionPriceDifference(Map<String, Session> sessionsList) {
        /*
         * Prints the difference in price of each product bought against the average price of all the products viewed
         * in the session.
         */
        System.out.println("Price Difference for Purchased Product by Session");
        for (Map.Entry<String, Session> session: sessionsList.entrySet()) {
            Session theSession = session.getValue();
            if (theSession.getBuyActions().size() > 0) {
                System.out.println(theSession.getSessionId());

                // Get the average price of all the viewed items in the session
                int totalPricesViewed = 0;
                int totalItemsViewed = 0;
                for (ViewAction viewAction: theSession.getViewActions()) {
                    totalPricesViewed += viewAction.getProduct().getPrice();
                    totalItemsViewed += 1;
                }
                double averageItemsViewedPrice = (double)totalPricesViewed / totalItemsViewed;

                // Print the difference in average viewed price and product bought price
                for (BuyAction buyAction: theSession.getBuyActions()) {
                    System.out.print("\t" + buyAction.getProduct().getId() + " ");
                    System.out.printf("%.1f", (double)buyAction.getProduct().getPrice() - averageItemsViewedPrice);
                    System.out.println();
                }
            }
        }
    }

    private static void  printCustomerItemViewsForPurchase(Map<String, List<Session>> sessionsFromCustomers) {
        /*
         * For each product that every customer bought, print the number of sessions in which that customer viewed the
         * product at least once.
         * NOTE: Prints the number of sessions in which the item was viewed not the total number of times the action was
         * viewed!
         */
        System.out.println("Number of Views for Purchased Product by Customer");
        for (Map.Entry<String, List<Session>> customer: sessionsFromCustomers.entrySet()) {
            List<Session> customerSessions = customer.getValue();

            // Print Customer ID if they have at least 1 value
            boolean hasPurchase = false;
            for (Session session: customerSessions) {
                if (session.getBuyActions().size() > 0) {
                    hasPurchase = true;
                }
            }
            if (hasPurchase) {System.out.println(customer.getKey());}

            for (Session session: customerSessions) {
                if (session.getBuyActions().size() > 0) {
                    for (BuyAction buyAction: session.getBuyActions()) {
                        String productId = buyAction.getProduct().getId();

                        // Get the number of sessions in which the product was viewed at least once.
                        int thisProductViewedCount = 0;
                        List<String> productViewedSessionId = new LinkedList<>();
                        for (Session thatSession: customerSessions) {
                            for (ViewAction viewAction: thatSession.getViewActions()) {
                                if (viewAction.getProduct().getId() == productId &&
                                        !productViewedSessionId.contains(viewAction.getSessionID())) {
                                    thisProductViewedCount += 1;
                                    productViewedSessionId.add(viewAction.getSessionID());
                                }
                            }
                        }
                    System.out.println("\t" + productId + " " + thisProductViewedCount);
                    }
                }
            }
        }
    }

    private static void printStatistics(Map<String, List<Session>> sessionsFromCustomers, Map<String, Session> sessionsList) {
        averageViewsWOPurchase(sessionsList);
        System.out.println();
        printSessionPriceDifference(sessionsList);
        System.out.println();
        printCustomerItemViewsForPurchase(sessionsFromCustomers);

      /* This is commented out as it will not work until you read
         in your data to appropriate data structures, but is included
         to help guide your work - it is an example of printing the
         data once propagated
         printOutExample(sessionsFromCustomer, viewsFromSession, buysFromSession);
      */
    }


    /* provided as an example of a method that might traverse your
       collections of data once they are written
       commented out as the classes do not exist yet - write them! */
    private static void printOutExample(
            final Map<String, List<String>> sessionsFromCustomer,
            final Map<String, List<ViewAction>> viewsFromSession,
            final Map<String, List<BuyAction>> buysFromSession) {
        //for each customer, get their sessions
        //for each session compute views
        for (Map.Entry<String, List<String>> entry :
                sessionsFromCustomer.entrySet()) {
            System.out.println(entry.getKey());
            List<String> sessions = entry.getValue();
            for (String sessionID : sessions) {
                System.out.println("\tin " + sessionID);
                List<ViewAction> theViewActions = viewsFromSession.get(sessionID);
                for (ViewAction thisViewAction : theViewActions) {
                    System.out.println("\t\tviewed " + thisViewAction.getProduct());
                }
            }
        }
    }

    //called in populateDataStructures
    private static void processFile(
            final Scanner input,
            final Map<String, List<Session>> sessionsFromCustomer,
            final Map<String, Session> sessionsList,
            final Map<String, Product> productsList) {
        while (input.hasNextLine()) {
            processLine(input.nextLine(), sessionsFromCustomer, sessionsList, productsList);
        }
    }

    private static void populateDataStructures(
            final String filename,
            final Map<String, List<Session>> sessionsFromCustomer,
            final Map<String, Session> sessionsList,
            final Map<String, Product> productsList)
            throws FileNotFoundException {
        try (Scanner input = new Scanner(new File(filename))) {
            processFile(input, sessionsFromCustomer, sessionsList, productsList);
        }
    }

    private static String getFilename(String[] args) {
        if (args.length < 1) {
            System.err.println("Log file not specified.");
            System.exit(1);
        }

        return args[0];
    }

    public static void main(String[] args) {
        /* Map from a customer id to a list of sessions associated with that customer. */
        final Map<String, List<Session>> sessionsFromCustomer = new HashMap<>();
        /* Map from a session id to session object. */
        final Map<String, Session> sessionsList     = new HashMap<>();
        // Map from a product id to a product object
        final Map<String, Product> productsList = new HashMap<>();

        final String filename = getFilename(args);

        try {
            populateDataStructures(filename, sessionsFromCustomer, sessionsList, productsList);
            printStatistics(sessionsFromCustomer, sessionsList);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
