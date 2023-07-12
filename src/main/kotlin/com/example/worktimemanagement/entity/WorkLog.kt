package com.example.worktimemanagement.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "work_logs")
data class WorkLog (
    @Id
    @Column(name = "work_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val workLogId: Int?,

    @Column(name = "work_log_user_id")
    val workLogUserId: Int,

    @Column(name = "work_log_date")
    val workLogDate: String,

    @Column(name = "work_log_start_time")
    val workLogStartTime: String,

    @Column(name = "work_log_end_time")
    val workLogEndTime: String,

    @Column(name = "work_log_seconds")
    val workLogSeconds: Int = 0
)