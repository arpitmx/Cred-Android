package com.cred.cool.loan.domain.models

data class Page(
    val openStateTitle: String,
    val openStateSubtitle: String,
    val cardHeader: String? = null,
    val cardDescription: String? = null,
    val cardMaxRange: Int? = null,
    val cardMinRange: Int? = null,
    val openStateFooter: String,
    val repaymentItems: List<RepaymentItem>? = null,

    val closedStateKey1: String? = null,
    val closedStateKey2: String? = null,

    val ctaText: String
)

data class RepaymentItem(
    val emi: String? = null,
    val duration: String? = null,
    val title: String,
    val subtitle: String,
    val tag: String? = null,
    val icon: String? = null,
    var isSelected : Boolean = false
)