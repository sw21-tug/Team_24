package at.tugraz.ist.guessingwords.data.service

interface Callback<T> {
    fun whenReady(data:T?)
}