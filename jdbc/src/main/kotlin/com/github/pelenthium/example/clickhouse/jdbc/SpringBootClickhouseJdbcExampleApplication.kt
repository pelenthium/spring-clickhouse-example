package com.github.pelenthium.example.clickhouse.jdbc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SpringBootClickhouseJdbcExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringBootClickhouseJdbcExampleApplication>(*args)
}

@RestController
@RequestMapping("/counters")
class VisitStatisticController(val jdbcTemplate: JdbcTemplate, val repository: CounterRepository) {

    @GetMapping("/top")
    fun topCounters(): List<Counter> {
        return jdbcTemplate.query("""            
          SELECT CounterID as counterId, count() AS count
                  FROM visits_v1 
                      GROUP BY CounterID 
                          ORDER BY count DESC 
                              LIMIT 10;
        """.trimIndent(), BeanPropertyRowMapper.newInstance(Counter::class.java))
    }

    @GetMapping("/")
    fun list(): List<Visit> {
        return jdbcTemplate.query("""            
          SELECT VisitID, LinkURL, CounterID
                  FROM visits_v1 LIMIT 10;
        """.trimIndent(), BeanPropertyRowMapper.newInstance(Visit::class.java))
    }

    @GetMapping("/top/{id}")
    fun getById(@PathVariable id: Long) = repository.findById(id)
    
    @PostMapping("/")
    fun create() {
        repository.save(Visit(
                linkUrl = "example.com",
                counterID = 912887
        ))
    }
}

@Table("visits_v1")
data class Visit(@Id @Column("VisitID") var id: Long? = 0, @Column("LinkURL") var linkUrl: String? = "", @Column("CounterID") var counterID: Int? = 0)

data class Counter(var counterId: Int = 0, var count: Long = 0)

class B {}
class C {}

@Repository
interface CounterRepository : CrudRepository<Visit, Long> {
    
}