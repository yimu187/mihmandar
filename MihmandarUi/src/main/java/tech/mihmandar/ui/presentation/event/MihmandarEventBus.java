package tech.mihmandar.ui.presentation.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import tech.mihmandar.ui.presentation.common.MihmandarApplication;

/**
 * Created by Murat on 8/31/2017.
 */
public class MihmandarEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        MihmandarApplication.get().getMihmandarEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        MihmandarApplication.get().getMihmandarEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        MihmandarApplication.get().getMihmandarEventbus().eventBus.unregister(object);
    }

    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }

}
