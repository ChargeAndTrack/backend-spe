## [0.5.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.4.0...0.5.0) (2026-03-01)

### Features

* **charging-stations:** add charging station and location serializable DTOs and models for the db ([1a27612](https://github.com/ChargeAndTrack/backend-spe/commit/1a276127077d809729b70cff7b437d0a95ea663f))
* **charging-stations:** add ChargingStationRepository and ChargingStationService interfaces; add ChargingStationService implementation ([e118fe8](https://github.com/ChargeAndTrack/backend-spe/commit/e118fe84afa42c2104cad3e56a0e946cd175be76))
* **charging-stations:** add data classes representing inputs for charging stations, add update charging station method ([173448b](https://github.com/ChargeAndTrack/backend-spe/commit/173448b2f18bcec73db99be7ca24cba6d8a44766))
* **charging-stations:** add get, put and delete charging stations routes to the Router; implement their handlers in the controller, service and repository ([c2ba5d3](https://github.com/ChargeAndTrack/backend-spe/commit/c2ba5d3471505e35229e6d5007730e85da14663d))
* **charging-stations:** add getNearbyChargingStations and getClosestChargingStation routes and implementations; add their input and DTO data classes ([e461dae](https://github.com/ChargeAndTrack/backend-spe/commit/e461dae1176da57210612f2c6382314935528115))
* **charging-stations:** add LocationImpl, ChargingStationImpl and DTOs validation; add companion object Factory for ChargingStationImpl; add getNewId function to ChargingStationRepository ([b8466c0](https://github.com/ChargeAndTrack/backend-spe/commit/b8466c0d81f2108e94882749bb99fb33a7b0b871))
* **charging-stations:** add MongoDbChargingStationRepository and ChargingStationsController with listChargingStations and addChargingStation routes and handlers; add AddChargingStationDTO ([d32c797](https://github.com/ChargeAndTrack/backend-spe/commit/d32c7972e2b2babbfbb6bf1a1dbafaab1beae1cf))
* **charging-stations:** add UpdateChargingStationDTO  and its conversion to input, rmove currentCarId from ChargingStationDTO and ChargingStationDbEntity, add AddChargingStationInput conversion to db entity ([ee3b0f3](https://github.com/ChargeAndTrack/backend-spe/commit/ee3b0f39c786f6b468ab68e2536c0eee1b2fd95a))
* **charging-stations:** change addChargingStation methods return type to nullable ChargingStation, add extension function 'assemblePath' to Router ([90beec6](https://github.com/ChargeAndTrack/backend-spe/commit/90beec6b45c1ecd6995a91a0a43ba60fe4dae27b))
* **charging-stations:** getNearbyChargingStation and getClosestChargingStation request data as query parameters; minor refactors ([fe37b50](https://github.com/ChargeAndTrack/backend-spe/commit/fe37b50903504b1f3de719d1fc0cfdd1a4119eb9))
* **charging-stations:** move domain interfaces to charging_station directory and add their implementation ([b9aa7fd](https://github.com/ChargeAndTrack/backend-spe/commit/b9aa7fd0f641d1b311e5657655050158c5b8cdbd))

### Bug Fixes

* **build:** add ktor server-status-pages dependency ([3742a6e](https://github.com/ChargeAndTrack/backend-spe/commit/3742a6eb13aa1f4cbaca13cfe19f23881ec55323))

### Documentation

* **charging-stations:** add charging stations openapi ([0f754b0](https://github.com/ChargeAndTrack/backend-spe/commit/0f754b0e482a3c27b415564001bbf6f0a73a3847))
* **charging-stations:** change unauthorized status code to 401 ([7ee9de3](https://github.com/ChargeAndTrack/backend-spe/commit/7ee9de3c679ba2c888a22498e0db8b19a91c512c))

### Tests

* **charging-stations:** add ChargingStationTest class and implement test for adding a charging station ([76c745e](https://github.com/ChargeAndTrack/backend-spe/commit/76c745e2c108c7c1d5fb810d6db8a35eebb26939))
* **charging-stations:** add tests for getting, deleting and updating a charging station by id and listing all the charging stations ([b0a4f5f](https://github.com/ChargeAndTrack/backend-spe/commit/b0a4f5f989b0ab815f6a1b29ef29bd0b3600b11a))
* **charging-stations:** replace kotlin test with kotest implementation ([0be45c7](https://github.com/ChargeAndTrack/backend-spe/commit/0be45c7ed8e85e5a95c59af4c7996cb2148d1e6c))

### Build and continuous integration

* add test dependencies and configure test task ([d521c4d](https://github.com/ChargeAndTrack/backend-spe/commit/d521c4dca8d510e41375f195ca2a62162b91d621))
* replace kotlin test with kotest dependencies and configure gradle libs.versions.toml file with versions, libraries, bundles and plugins ([12390ba](https://github.com/ChargeAndTrack/backend-spe/commit/12390ba5fe74c3b7aba5e8a3ed9ec7316507e217))

### Refactoring

* **charging-stations:** move ChargingStationDTOs validation in a dedicated method and ChargingStationController common responding logic in an extension function ([3ea9cd5](https://github.com/ChargeAndTrack/backend-spe/commit/3ea9cd5d93cc390a5961d74f14b51cab71424826))
* **charging-stations:** move extension functions for conversions where serializable data classes are defined ([188da1c](https://github.com/ChargeAndTrack/backend-spe/commit/188da1c5d8556b93b46e6d25d7fb6d622e61d094))
* **charging-stations:** use AppExceptions ([e973546](https://github.com/ChargeAndTrack/backend-spe/commit/e973546576b0f7f0f5541c11368aef1fe9b8b145))
* **test:** create common buildRequest extension function and charging station path assemble function ([f75566b](https://github.com/ChargeAndTrack/backend-spe/commit/f75566b93900d0671dd80af3665f9210bc7f2517))

## [0.4.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.3.0...0.4.0) (2026-03-01)

### Features

* add StatusPages plugin and error handling in Server; update CarsTest accordingly ([9185178](https://github.com/ChargeAndTrack/backend-spe/commit/9185178b9205b2a34de5330abe4abfe8df725c85))
* **cars:** add AddCarInput validation and create Factory for Car; move some addCar logic from UserRepository to CarService, so add getNewId method to UserRepository interface and addCar method to User interface ([4b2c126](https://github.com/ChargeAndTrack/backend-spe/commit/4b2c12600790a361dc8714b111f159ccd2a4b065))
* **cars:** add cars routes to Router and add CarController implementing getCars, addCar and deleteCar methods ([635c2a6](https://github.com/ChargeAndTrack/backend-spe/commit/635c2a6c94ff1865e06cbd584f2fc9015cdc056c))
* **cars:** add CarService interface and implementation ([f6b2c29](https://github.com/ChargeAndTrack/backend-spe/commit/f6b2c2950175d06b45bb1190c68406d214f71b28))
* **cars:** add DTOs validation ([dc12a28](https://github.com/ChargeAndTrack/backend-spe/commit/dc12a2834d20887a3af76e9a0657b49cccd853c0))
* **cars:** add UpdateCarInput validation, updateCar method to user interface and plate check in UserImpl; refactor updateCar method in CarServiceImpl ([85bc9c8](https://github.com/ChargeAndTrack/backend-spe/commit/85bc9c81421fb8f2c4d343e0578c8d8fc4e7c090))
* **cars:** implement getCar functionality ([a55956a](https://github.com/ChargeAndTrack/backend-spe/commit/a55956a173b1f07c128f22f08b8aaed6397c693f))
* **cars:** implement updateCar functionality and small refactor; rename some car tests ([525aa2e](https://github.com/ChargeAndTrack/backend-spe/commit/525aa2e47858f15689292aa99e593c1c17bbca85))
* **cars:** update UserRepository interface with car methods and implement getCars, addCar and deleteCar methods in UserRepositoryImpl ([057204f](https://github.com/ChargeAndTrack/backend-spe/commit/057204f55160bde856ad966f50714dbb4a6ffd89))

### Documentation

* **cars:** add cars openapi ([11b884d](https://github.com/ChargeAndTrack/backend-spe/commit/11b884daac00246246cdf8951a5bfe215703fa40))

### Tests

* add user tests ([9dd9dd6](https://github.com/ChargeAndTrack/backend-spe/commit/9dd9dd6d35d67b1843d8aee1ecd840d42adde86e))
* **cars:** add AddCarDTO and prepare some car tests ([51f094e](https://github.com/ChargeAndTrack/backend-spe/commit/51f094e8dbbd1e8eec8a0d22d49d0d480f3e3375))
* **cars:** refactor UserTest and CarsTest using kotest instead of kotlin test ([59ffe1c](https://github.com/ChargeAndTrack/backend-spe/commit/59ffe1c48994fa802bbb85cc3d42898ed688fb91))

### Build and continuous integration

* add ktor-server-status-pages dependency ([85e9ba6](https://github.com/ChargeAndTrack/backend-spe/commit/85e9ba68de372ae60f4f9727063ca616bf609109))
* add test dependencies ([f7bbdb3](https://github.com/ChargeAndTrack/backend-spe/commit/f7bbdb3acc77fdf5a2ca464e9bcffdf07a56c961))
* replace kotlin test with kotest dependencies ([3f06b31](https://github.com/ChargeAndTrack/backend-spe/commit/3f06b31b307888ed1958170cd3e63b5eb01207b3))
* show standard streams in tests ([0b6a333](https://github.com/ChargeAndTrack/backend-spe/commit/0b6a3337dd3c61eac9b8ac0c2d45aa30966aa191))

### Refactoring

* **cars:** add specific app exceptions ([c03ff85](https://github.com/ChargeAndTrack/backend-spe/commit/c03ff85a8b0ebbc1eab120061eb0c232e5cb83ff))
* **cars:** move CarInputs from application to domain, add update method to Car and move car retrieval in CarService ([4c1e26e](https://github.com/ChargeAndTrack/backend-spe/commit/4c1e26e661ecc8cff81018befdd71fadfe4c0ad8))
* **cars:** move validate method into CarImpl, using it in init, and move Factory into CarImpl; organize better UserDTO by moving methods inside their respective classes ([260d99c](https://github.com/ChargeAndTrack/backend-spe/commit/260d99cce81624edea0ab69e0ac5284274a344bf))
* move toDTO methods from UserController to UserDTO ([550e73e](https://github.com/ChargeAndTrack/backend-spe/commit/550e73eb178aff6d4e8f7eebff05b32c90d38ef3))

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
