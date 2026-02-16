## [0.3.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.2.0...0.3.0) (2026-02-16)

### Features

* **login:** add authentication with JWT, application.conf and UserDTO ([6780101](https://github.com/ChargeAndTrack/backend-spe/commit/6780101c44aac50ce6878a3a8c1617da0fc5ec51))
* **login:** add UserService, UserRepository and implementations, add user domain implementations and add login route, handler and DTOs ([81c32b4](https://github.com/ChargeAndTrack/backend-spe/commit/81c32b40f41cae89364593f48d77cfc0d3d9cbc9))
* **login:** add verify admin role as JWT configuration ([0e17f52](https://github.com/ChargeAndTrack/backend-spe/commit/0e17f5219901a16896a76eaf00c7c0e71869a6d8))

### Bug Fixes

* **deps:** add serialization dependency and plugin, add and install server content negotiation ([7f37dbd](https://github.com/ChargeAndTrack/backend-spe/commit/7f37dbdad54fb8f620eddc20fd84953292ba5bd6))

### Documentation

* add openapi ([2e2585b](https://github.com/ChargeAndTrack/backend-spe/commit/2e2585b496698a0de8ad6b8eaa92c75c668b7054))

### Build and continuous integration

* add ktor authentication dependencies ([f736f00](https://github.com/ChargeAndTrack/backend-spe/commit/f736f0038161cb4138e41a51d2707e1e41230d6f))

### Refactoring

* move ktor deployment values in application.conf, extract extension function for jwt configuration ([54312c9](https://github.com/ChargeAndTrack/backend-spe/commit/54312c977cd9b1260c828cd531015c679bcb6bcb))
* move user domain in user package ([d2fa1bd](https://github.com/ChargeAndTrack/backend-spe/commit/d2fa1bd34378d696bc87900bff42c6e0ee712a1c))

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
