package com.dash.infra.repository

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.dialect.PostgreSQL10Dialect
import java.sql.Types

internal class DashDatabaseDialect : PostgreSQL10Dialect() {
    init {
        registerHibernateType(Types.OTHER, JsonBinaryType::class.java.name)
    }
}
