package br.com.cronogramafiv.domain.model

/**
 * Defines which date the user used as the reference for schedule generation.
 */
enum class ScheduleAnchor {
    PROTOCOL_START,
    EMBRYO_TRANSFER,
    EXPECTED_BIRTH,
}
