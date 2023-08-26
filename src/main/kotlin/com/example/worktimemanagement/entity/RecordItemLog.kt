package com.example.worktimemanagement.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Entity
@Table(name = "record_item_logs")
data class RecordItemLog (
    @Id
    @Column(name = "record_item_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recordItemLogId: Int,

    @Column(name = "record_item_id")
    @NotNull
    val recordItemId: Int,

    @Column(name = "record_item_log_date")
    @NotBlank
    @field: Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])", message = "Invalid date format")
    val recordItemLogDate: String,

    @Column(name = "record_item_log_start_time")
    @NotBlank
    @field: Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Invalid date format")
    val recordItemLogStartTime: String,

    @Column(name = "record_item_log_end_time")
    @NotBlank
    @field: Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) (([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]|24:00:00)", message = "Invalid date format")
    val recordItemLogEndTime: String,

    @Column(name = "record_item_log_seconds")
    @NotNull
    val recordItemLogSeconds: Int = 0
)