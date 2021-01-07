package com.myituneapplication.util


fun Double.toCurrency(code: String): String {
    return "$this $code"
}