package io.polyglotapps.user;

import io.polyglotapps.system.EntityCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class UserEventListener {

    @EventListener
    @Async
    void onUserCreated(EntityCreatedEvent<User> event) {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("User is created: " + event.getSource());
    }

}
