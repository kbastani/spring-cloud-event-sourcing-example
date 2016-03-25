package demo.listeners;

import demo.domain.BaseEntity;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class BeforeSaveListener extends AbstractMongoEventListener<BaseEntity> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseEntity> event) {

        DateTime timestamp = new DateTime();

        // Add a timestamp to the created date if it does not yet exist
        if (event.getSource().getCreatedAt() == null)
            event.getSource().setCreatedAt(timestamp);

        // Update the timestamp to the current time
        event.getSource().setLastModified(timestamp);

        super.onBeforeSave(event);
    }
}
