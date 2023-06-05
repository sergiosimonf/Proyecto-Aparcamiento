package dev.sergiosf.proyectoaparcamiento.repositories.base

interface BaseRepository<T> {
    fun findAll(): List<T>
    fun save(entity: T): T
    fun saveAll(entity: List<T>): List<T>
}