package com.cred.cool.loan.domain.models

data class Plan(val rate : String, val duration : String, var isRecommended : Boolean, var isSelected : Boolean = false)