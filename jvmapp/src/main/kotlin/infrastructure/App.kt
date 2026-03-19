package infrastructure

import application.charging_station.ActiveRechargeRepository
import application.charging_station.ActiveRechargeRepositoryImpl
import application.charging_station.ChargingStationRepository
import application.charging_station.ChargingStationService
import application.charging_station.ChargingStationServiceImpl
import application.charging_station.GeocodingPort
import application.charging_station.LocationServiceImpl
import application.charging_station.QueryParsingPort
import application.charging_station.RechargeEventObserver
import application.charging_station.RechargeRepository
import application.charging_station.RechargeService
import application.charging_station.RechargeServiceImpl
import application.charging_station.SearchChargingStationsService
import application.charging_station.SearchChargingStationsServiceImpl
import application.user.CarService
import application.user.CarServiceImpl
import application.user.UserRepository
import application.user.UserService
import application.user.UserServiceImpl
import infrastructure.charging_station.ChargingStationsController
import infrastructure.charging_station.HuggingFaceQueryParsingAdapter
import infrastructure.charging_station.LlmController
import infrastructure.charging_station.LocationController
import infrastructure.charging_station.MongoDbChargingStationRepository
import infrastructure.charging_station.MongoDbRechargeRepository
import infrastructure.charging_station.NominatimGeocodingAdapter
import infrastructure.charging_station.RechargeController
import infrastructure.charging_station.SocketIORechargeEventObserver
import infrastructure.user.CarController
import infrastructure.user.MongoDbUserRepository
import infrastructure.user.UserController

private val userRepository: UserRepository = MongoDbUserRepository()
private val chargingStationRepository: ChargingStationRepository = MongoDbChargingStationRepository()
private val rechargeRepository: RechargeRepository = MongoDbRechargeRepository()
private val activeRechargeRepository: ActiveRechargeRepository = ActiveRechargeRepositoryImpl()

private val rechargeEventObserver: RechargeEventObserver = SocketIORechargeEventObserver(Socket.server)
private val geocodingPort: GeocodingPort = NominatimGeocodingAdapter()
private val queryParsingPort: QueryParsingPort = HuggingFaceQueryParsingAdapter()

private val userService: UserService = UserServiceImpl(userRepository)
private val carService: CarService = CarServiceImpl(userRepository)
private val chargingStationService: ChargingStationService = ChargingStationServiceImpl(chargingStationRepository)
private val rechargeService: RechargeService = RechargeServiceImpl(rechargeRepository, chargingStationService,
    carService, rechargeEventObserver, activeRechargeRepository)
private val locationService = LocationServiceImpl(geocodingPort)
private val searchService: SearchChargingStationsService =
    SearchChargingStationsServiceImpl(chargingStationService, locationService, queryParsingPort)

private val userController = UserController(userService)
private val carController = CarController(carService, rechargeService)
private val chargingStationsController = ChargingStationsController(chargingStationService, rechargeService)
private val rechargeController = RechargeController(rechargeService, chargingStationService, carService)
private val locationController = LocationController(locationService)
private val llmController = LlmController(searchService)

private val router: Router = Router(userController, carController, chargingStationsController, rechargeController,
    locationController, llmController)

fun main() {
    Server(router.module).start()
}
