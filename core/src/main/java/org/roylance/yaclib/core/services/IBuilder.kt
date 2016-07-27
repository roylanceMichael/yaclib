package org.roylance.yaclib.core.services

interface IBuilder<out T> {
    fun build(): T
}