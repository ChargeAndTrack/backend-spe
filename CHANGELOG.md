## [0.2.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.1.0...0.2.0) (2026-02-09)

### Features

* add MongoDB init script, add Dockerfiles for mongodb and jvmapp, add docker-compose.yml ([415ad53](https://github.com/ChargeAndTrack/backend-spe/commit/415ad53e311b6dcd71153265a3a46e3ca8fdd6c8))
* **domain:** model domain interfaces ([031d369](https://github.com/ChargeAndTrack/backend-spe/commit/031d369dee00322a2e0a5fd11a0a7653749a428d))
* **infrastructure:** add MongoDB singleton and remove unused imports ([1dd79a1](https://github.com/ChargeAndTrack/backend-spe/commit/1dd79a1704cdd2523db49959efabb5ee756ed27b))
* **infrastructure:** add Server, Router and example of UserController ([ae768da](https://github.com/ChargeAndTrack/backend-spe/commit/ae768da3b6ee6914ad32bbf4b7e88e712939f7e9))

### Documentation

* add domain diagram ([d7c836c](https://github.com/ChargeAndTrack/backend-spe/commit/d7c836ce9100712730489cf6fe3b59a480054b0b))

### Build and continuous integration

* add MongoDB dependencies ([001fb00](https://github.com/ChargeAndTrack/backend-spe/commit/001fb005ad6af8266702d15fc4f1915719351880))
* include jvmapp ([690df29](https://github.com/ChargeAndTrack/backend-spe/commit/690df297c1988b84203baba920e3d075de10cfb7))
* replace gradle build with docker compose step ([5bfd6f3](https://github.com/ChargeAndTrack/backend-spe/commit/5bfd6f349bbbc51f8c2544976043d67d9654a3b9))

### Refactoring

* move src folder and build.gradle.kts into jvmapp folder, add ktor fatJar configuration ([0153428](https://github.com/ChargeAndTrack/backend-spe/commit/0153428fae46358a69806d8992054e5af66c3e87))
