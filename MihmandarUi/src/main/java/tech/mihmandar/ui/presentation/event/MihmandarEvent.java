package tech.mihmandar.ui.presentation.event;

import tech.mihmandar.core.common.enums.EnumGender;
import tech.mihmandar.core.common.enums.EnumSoftwareLanguages;
import tech.mihmandar.ui.presentation.view.ViewType;

/**
 * Created by Murat on 8/31/2017.
 */
public abstract class MihmandarEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;
        private final boolean newUser, rememberMe;

        public UserLoginRequestedEvent(final String userName,
                                       final String password,
                                       final  boolean newUser,
                                       final boolean rememberMe) {
            this.userName = userName;
            this.password = password;
            this.newUser = newUser;
            this.rememberMe = rememberMe;
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

        public boolean isRememberMe() {
            return rememberMe;
        }
    }

    public static class BrowserResizeEvent {
        final int height;
        final int width;

        public BrowserResizeEvent(int height, int width) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

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
        public ProfileUpdatedEvent() {}
    }

    public static class SoftwareLanguageChagedEvent {
        private EnumSoftwareLanguages softwareLanguage;
        public SoftwareLanguageChagedEvent(EnumSoftwareLanguages softwareLanguage) {
            this.softwareLanguage = softwareLanguage;
        }

        public EnumSoftwareLanguages getSoftwareLanguage() {
            return softwareLanguage;
        }
    }
}
