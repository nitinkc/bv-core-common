package com.bit.velocity.common.event;

/**
 * Event publisher interface for domain events.
 * Implementations can use Kafka, NATS, or RabbitMQ based on messaging requirements.
 * 
 * Follows patterns defined in CROSS_OBSERVABILITY_AND_TESTING.md for event-driven architecture.
 */
public interface EventPublisher {

    /**
     * Publishes an event to the configured message broker
     * 
     * @param eventEnvelope The event to publish
     */
    void publish(EventEnvelope eventEnvelope);

    /**
     * Publishes an event to a specific topic/queue
     * 
     * @param topic The destination topic
     * @param eventEnvelope The event to publish
     */
    void publish(String topic, EventEnvelope eventEnvelope);
}