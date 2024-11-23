package com.cs407.tapntab

data class MenuItem(val icon: Int, val name: String, val categories: List<String>?, val cost: Double, val dispCost: String, val onClick: () -> Unit)

