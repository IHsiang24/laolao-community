package com.xiangkai.community.event;

import com.xiangkai.community.model.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private EventProducerAdaptor eventProducerAdaptor;

    public void fireEvent(Event event) {
        eventProducerAdaptor.sendMessageWithRetry(event.getTopic(), event.toJson());
    }
}
