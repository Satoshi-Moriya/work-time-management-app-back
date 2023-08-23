package com.example.worktimemanagement.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
@Table(
    name = "record_items",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "record_item_name"])]
)
data class RecordItem (
    @Id
    @Column(name = "record_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recordItemId: Int,

    @Column(name = "user_id")
    @NotNull
    val userId: Int,

    @Column(name = "record_item_name")
    @NotBlank
    val recordItemName: String,
)