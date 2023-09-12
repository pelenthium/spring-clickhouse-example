package com.github.pelenthium.example.clickhouse.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Time
import java.util.*

@SpringBootApplication
class SpringBootClickhouseRepositoryExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringBootClickhouseRepositoryExampleApplication>(*args)
}


@Entity
@Table(name = "visits_v1")
data class VisitEntity(
    @Id @Column(name = "VisitID")
    var id: Long,
    var counterId: Int,
    var startDate: Date,
    var starttime: Time,
    var userID: Long,
    var age: Int,
    var linkurl: String
)

@Repository
interface VisitRepository : CrudRepository<VisitEntity, Long> {

}

@RestController
@RequestMapping("/visits")
class VisitStatisticController(val repository: VisitRepository) {
    @GetMapping("/total")
    fun total() = repository.count()
}