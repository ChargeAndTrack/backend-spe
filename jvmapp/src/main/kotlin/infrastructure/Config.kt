package infrastructure

object Config {
    object Deployment {
        var host: String? = null
        var port: Int? = null
        var rootPath: String? = null
    }

    object Jwt {
        var secret: String? = null
    }

    object Llm {
        var hfSecret: String? = null
        var hfModel: String? = null
        var temperature: Double? = null
        var maxTokens: Int? = null
    }
}