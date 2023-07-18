package com.github.pelenthium.example.springbootclickhouseexample

import com.clickhouse.jdbc.ClickHouseDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.sql.DataSource

@SpringBootApplication
class SpringBootClickhouseJdbcExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringBootClickhouseJdbcExampleApplication>(*args)
}

@Configuration
class ClickHouseConfiguration(@Value("\${clickhouse.url}") val url: String) {
    @Bean
    fun dataSource() = HikariDataSource(
            HikariConfig().apply {
                dataSource = ClickHouseDataSource(url, Properties())
            }
    )

    @Bean
    fun template(@Autowired source: DataSource): JdbcTemplate? {
        return JdbcTemplate(source)
    }
}

@RestController
@RequestMapping("/counters")
class VisitStatisticController(val jdbcTemplate: JdbcTemplate) {

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
}

data class Counter(var counterId: Int = 0, var count: Long = 0)