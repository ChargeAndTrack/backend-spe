## [1.0.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.7.0...1.0.0) (2026-03-18)

### ⚠ BREAKING CHANGES

* **recharge:** add ChargingStationUpdated domain event and its usage, rename ChargingStationUpdateEvent to ChargingStationUpdatedEvent

### Features

* add CarRechargingDTO; getChargingStation, listChargingStations, getNearbyChargingStations and getClosestChargingStations respond with ChargingStationRechargingDTO; adjust tests ([eef4b1f](https://github.com/ChargeAndTrack/backend-spe/commit/eef4b1f9b94371bca14ed8c315ed56b55d8ae622))
* **recharge:** add ChargingStationUpdated domain event and its usage, rename ChargingStationUpdateEvent to ChargingStationUpdatedEvent ([5702664](https://github.com/ChargeAndTrack/backend-spe/commit/570266483eab879e833da714efcb8918801a3152))

### Bug Fixes

* **server:** set encodeDefaults as true in ContentNegotiation JSON options ([4fd1a4a](https://github.com/ChargeAndTrack/backend-spe/commit/4fd1a4a491f6c4857ed439ed3de9bbaf74a1e66e))

### Documentation

* add CarRecharging and ChargingStationRecharging components schemas in openapi, rename charging-station-update to charging-station-updated in asyncapi ([f7f5236](https://github.com/ChargeAndTrack/backend-spe/commit/f7f5236b34f6acabcb1e575c30c01e7feabbd919))

## [0.7.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.6.0...0.7.0) (2026-03-17)

### ⚠ BREAKING CHANGES

* **recharge:** add start and stop recharge in openapi; add asyncapi

### Features

* add currentChargingStationId in CarDTO and currentCarBattery in StartRechargeLogicDTO; update car battery when starting a recharge; fix conversion in PERCENT_CHANGE ([5b884bb](https://github.com/ChargeAndTrack/backend-spe/commit/5b884bb24496234c2727e8c513652a88c3bd98d6))
* **charging-stations:** charging station power must be between 1 and 100 to be valid instead of greater than 0; add default null values to nullable properties of input data classes ([88da5c5](https://github.com/ChargeAndTrack/backend-spe/commit/88da5c5c9121b16177539a493c4951906ccdca85))
* install CORS plugin in Server ([745fbff](https://github.com/ChargeAndTrack/backend-spe/commit/745fbfff6e4500366515c977e51be2ab92e885af))
* **mongodb:** add recharges collection ([adeaa67](https://github.com/ChargeAndTrack/backend-spe/commit/adeaa670e8f44fa0d6e4d792a2197a42e3d4f16e))
* **recharge:** add ChargingStationRechargingDTO and its conversion from ChargingStation; change getChargingStation to respond with the right DTO based on a parameter ([76cde80](https://github.com/ChargeAndTrack/backend-spe/commit/76cde8030d32aa81110eb585cf4c41e3a3bc68e7))
* **recharge:** add increment car battery methods and IncrementCarBatteryInput data class ([11c0aa0](https://github.com/ChargeAndTrack/backend-spe/commit/11c0aa0fafa91e9913eba15c49e95b1729d0437e))
* **recharge:** add methods to Recharge interface and their implementation; add RechargeEvent interface so RechargeCompleted and RechargeUpdate domain events; add StartRechargeLogicInput data class; add RechargeObserver interface ([516553d](https://github.com/ChargeAndTrack/backend-spe/commit/516553d65c699e58c9ad7233f87f4b4b01c7e5ad))
* **recharge:** add RechargeRepository, RechargeService interfaces and the service implementation ([c8c0be7](https://github.com/ChargeAndTrack/backend-spe/commit/c8c0be717ea8e7a6d1d0387007dec85c3fd58685))
* **recharge:** add socketio server, RechargeEventObserver and its socketio implementation, EventType enum and SocketIOEvent sealed class ([542c6bb](https://github.com/ChargeAndTrack/backend-spe/commit/542c6bbf127822f49c53ca02cc2062ab7ee80b48))
* **recharge:** add start and stop recharge routes; add recharge DTOs and RechargeDbEntity data classes; add MongoDbRechargeRepository and RechargeController ([bdbc1cd](https://github.com/ChargeAndTrack/backend-spe/commit/bdbc1cdcb836b618e0dba63579f472250c3895ee))
* **recharge:** add StartRechargeLogicDTO; change start and stop recharge methods using a mutable map of active recharges and implement notifyRechargeEvent method because now RechargeService is a RechargeObserver ([f114e9b](https://github.com/ChargeAndTrack/backend-spe/commit/f114e9b113483ac0c74336f8840582689130d6c5))
* **recharge:** replace Car and ChargingStation references with their ids in Recharge; add RechargeInputs data classes ([da3df32](https://github.com/ChargeAndTrack/backend-spe/commit/da3df3229fc5a39f2f717f2a3967dbdd68d34882))

### Bug Fixes

* **recharge:** charging station power required greater than 0 in charging station DTOs and StartRechargeLogicDTO; replace withContext with CoroutineScope in RechargeImpl start method; throw NotFoundException in RechargeServiceImpl stopRecharge method if recharge not present; change the expected charging station in recharge as not available ([6ebaac7](https://github.com/ChargeAndTrack/backend-spe/commit/6ebaac74c09717303e586156a2e7fbd2d34fa6e8))
* **recharge:** fix currentBattery update and job's stop when recharge complete in RechargeImpl; update charging station availability when starting a recharge and add RechargeEventObserver property to RechargeServiceImpl; refactor controllers into classes ([7c96162](https://github.com/ChargeAndTrack/backend-spe/commit/7c961626eece9b5a06976e3de39417ef5f06f5b7))

### Documentation

* **recharge:** add start and stop recharge in openapi; add asyncapi ([3e10b9a](https://github.com/ChargeAndTrack/backend-spe/commit/3e10b9a6c5301e7bddf9e8f56159d61fb5559a32))
* **recharge:** replace Car and ChargingStation references with their ids in Recharge in domainDiagram ([b9bb7ed](https://github.com/ChargeAndTrack/backend-spe/commit/b9bb7edde62a848e7fbe82ab69c0ccef45e1d953))

### Tests

* **charging-stations:** adjust get charging station tests based on getChargingStation change ([5f1d8ba](https://github.com/ChargeAndTrack/backend-spe/commit/5f1d8bafba80a1fee330648373b1aeeed2f671a3))
* **recharge:** add RechargeTest ([8f2f639](https://github.com/ChargeAndTrack/backend-spe/commit/8f2f639c9a45ae2c1c43b6ce91f1fc33aca9c837))

### Build and continuous integration

* add ktor server cors dependency ([da1ca10](https://github.com/ChargeAndTrack/backend-spe/commit/da1ca105f8ad95fff36885544d461ea9ad0b5dec))
* add netty socketio dependency and expose port 3001 from jvmapp service ([07553f1](https://github.com/ChargeAndTrack/backend-spe/commit/07553f1c2c36a82d1393e7eec9f5a60e1aff675a))

### Refactoring

* **recharge:** move validateRecharge extension function to RechargeDTO file and its call to RechargeController ([a4afac3](https://github.com/ChargeAndTrack/backend-spe/commit/a4afac3a9644b521a333afb39f2bee82a97ac089))
* **recharge:** replace Collection with Array as event class ([51a3cb2](https://github.com/ChargeAndTrack/backend-spe/commit/51a3cb2752d413d5e33d099b261d86b652fbc6f7))

## [0.6.0](https://github.com/ChargeAndTrack/backend-spe/compare/0.5.1...0.6.0) (2026-03-08)

### Features

* **location:** add location routes, empty LocationController and new interfaces ([cf29ebb](https://github.com/ChargeAndTrack/backend-spe/commit/cf29ebb334c3dc8c8a2c9219a995bea01c695432))
* **location:** implement resolve functionality ([24559a6](https://github.com/ChargeAndTrack/backend-spe/commit/24559a6b5e1961c3ac4894ced959f96c6abb724a))
* **location:** implement reverse functionality, minor refactor ([7f0ab45](https://github.com/ChargeAndTrack/backend-spe/commit/7f0ab457b3b9d0035d646e3a8b015cdb08f455f1))

### Build and continuous integration

* ktor-client dependencies not only in testing ([5cbe9d4](https://github.com/ChargeAndTrack/backend-spe/commit/5cbe9d4a99c0549b033663fc0dbf66f34baba427))

### Refactoring

* add default message in InternalErrorException ([8634da7](https://github.com/ChargeAndTrack/backend-spe/commit/8634da7f05af0dfc09d894c90c8b84b7660ca193))

## [0.5.1](https://github.com/ChargeAndTrack/backend-spe/compare/0.5.0...0.5.1) (2026-03-03)

### Bug Fixes

* **charging-stations:** throw InvalidInputException in case of DTOs validation failure and in ChargingStationController respond with BadRequest status code in case of error ([78f3f49](https://github.com/ChargeAndTrack/backend-spe/commit/78f3f49fa486723541316af2b208d9180b15cd80))

### Tests

* **charging-stations:** refactor tests into contexts; add tests for failing operations; delete all charging stations before each context execution ([11683ec](https://github.com/ChargeAndTrack/backend-spe/commit/11683ecfecf5a34f0352ea19f9b592ef2028c5d5))

### Build and continuous integration

* add test steps to build-and-test workflow, configure healthcheck to health endpoint ([0ea9d2e](https://github.com/ChargeAndTrack/backend-spe/commit/0ea9d2ed3012e75760a78701baf9bc155a61a71a))

### Refactoring

* **cars:** move constants into private companion objects ([4e3366c](https://github.com/ChargeAndTrack/backend-spe/commit/4e3366c8065f8ef7b2ccefc906ce6328879c9d2c))
* **cars:** remove some println and minor refactor ([77fdcf7](https://github.com/ChargeAndTrack/backend-spe/commit/77fdcf770e1cea8828ef85c5296d62108fd7da83))
* move QueryDTO interface in infrastructure package and use it in UserDTO ([3b27aed](https://github.com/ChargeAndTrack/backend-spe/commit/3b27aed079ab2b43af7aef11160839f537aab10c))
* **test:** move common setup in a separate object ([91d98fb](https://github.com/ChargeAndTrack/backend-spe/commit/91d98fbb80b3de77882554e9d2d9217962674645))
* **user:** rename UserRepositoryImpl as MongoDbUserRepository ([0d4e0ad](https://github.com/ChargeAndTrack/backend-spe/commit/0d4e0ad6f7f971364f38a4438a79330fad842127))

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
