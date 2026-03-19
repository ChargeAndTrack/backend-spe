package infrastructure

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig

object Config {
    private val config = HoconApplicationConfig(ConfigFactory.load())

    object Deployment {
        val host: String = config.property("ktor.deployment.host").getString()
        val port: Int = config.property("ktor.deployment.port").getString().toInt()
        val rootPath: String = config.property("ktor.deployment.rootPath").getString()
    }

    object Jwt {
        val secret: String = config.property("jwt.secret").getString()
    }

    object Llm {
        val hfSecret: String = config.property("llm.hfSecret").getString()
        val hfModel: String = config.property("llm.hfModel").getString()
        val temperature: Double = config.property("llm.temperature").getString().toDouble()
        val maxTokens: Int = config.property("llm.maxTokens").getString().toInt()
    }
}