package tech.mihmandar.ui.presentation.event;

import tech.mihmandar.ui.presentation.view.ViewType;

/**
 * Created by Murat on 8/31/2017.
 */
public abstract class MihmandarEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;
        private final boolean newUser;

        public UserLoginRequestedEvent(final String userName,
                                       final String password,
                                       final  boolean newUser) {
            this.userName = userName;
            this.password = password;
            this.newUser = newUser;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public boolean isNewUser() {
            return newUser;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

//    public static final class TransactionReportEvent {
//        private final Collection<Transaction> transactions;
//
//        public TransactionReportEvent(final Collection<Transaction> transactions) {
//            this.transactions = transactions;
//        }
//
//        public Collection<Transaction> getTransactions() {
//            return transactions;
//        }
//    }
//
    public static final class PostViewChangeEvent {
        private final ViewType view;

        public PostViewChangeEvent(final ViewType view) {
            this.view = view;
        }

        public ViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }
}
