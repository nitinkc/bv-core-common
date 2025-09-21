package com.bit.velocity.common.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Standard event envelope following the event contract pattern defined in
 * event-contracts/README.md and CROSS_EVENT_CONTRACTS_AND_VERSIONING.md
 * 
 * Naming convention: <domain>.<context>.<entity>.<eventType>.v<majorVersion>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope {

    /**
     * Unique identifier for this event instance
     */
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    /**
     * Event type following naming convention: domain.context.entity.eventType.vN
     * Examples: ecommerce.catalog.product.created.v1, chat.messaging.message.sent.v1
     */
    private String eventType;

    /**
     * Source system or service that generated the event
     */
    private String source;

    /**
     * Timestamp when the event occurred
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Event schema version for compatibility
     */
    private String version;

    /**
     * Correlation ID for tracing across services
     */
    private String correlationId;

    /**
     * User or system that triggered the event
     */
    private String triggeredBy;

    /**
     * Event payload - domain-specific data
     */
    private Object data;

    /**
     * Additional metadata for routing, filtering, or context
     */
    private Map<String, String> metadata;

    /**
     * Creates a new event envelope with standard defaults
     */
    public static EventEnvelope create(String eventType, String source, Object data) {
        return EventEnvelope.builder()
                .eventType(eventType)
                .source(source)
                .data(data)
                .version("v1")
                .build();
    }
}