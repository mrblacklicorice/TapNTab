package com.cs407.tapntab

import com.google.firebase.Timestamp

data class PastTab(
    val tabName: String,
    val tabTotal: Double,
    val timestamp: Timestamp
)