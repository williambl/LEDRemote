package com.williambl.led_remote

import android.content.Context
import android.hardware.ConsumerIrManager
import java.util.BitSet
import kotlin.experimental.inv

fun encode(address: Byte, command: Byte): List<Int> {
    val intervals = mutableListOf<Int>()

    intervals.add(9000) // starts with 9ms on
    intervals.add(4500) // 4.5ms off

    intervals.addAll(encode(BitSet.valueOf(byteArrayOf(
        address,
        address.inv(),
        command,
        command.inv()
    ))))

    return intervals
}

fun encode(bitset: BitSet): List<Int> {
    val intervals = mutableListOf<Int>()

    for (i in 0 until 8) {
        intervals.add(560)
        intervals.add(if (bitset[i]) 1680 else 560)
    }

    return intervals
}

fun transmit(context: Context, address: Byte, command: Byte) {
    val manager = context.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager

    manager.transmit(38000, encode(address, command).toIntArray())
}