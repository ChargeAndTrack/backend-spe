package infrastructure.charging_station

import application.charging_station.RechargeRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters.*
import common.Adapter
import domain.InternalErrorException
import domain.InvalidInputException
import domain.NotFoundException
import domain.charging_station.Recharge
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull

@Adapter
class MongoDbRechargeRepository : RechargeRepository {

    private companion object {
        const val RECHARGE_NOT_FOUND_MESSAGE = "Recharge not found"
        const val CAR_ALREADY_IN_CHARGE = "Car already in charge"
        const val OPERATION_FAILED_MESSAGE = "An unexpected error occurred while performing the operation"
    }

    private val recharges = MongoDb.database.getCollection<RechargeDbEntity>("recharges")

    override suspend fun getChargingStationIdByCarId(carId: String): String? = execute {
        recharges.find(eq("carId", carId)).firstOrNull()?.chargingStationId
    }

    override suspend fun getCarIdByChargingStationId(chargingStationId: String): String? = execute {
        recharges.find(eq("chargingStationId", chargingStationId)).firstOrNull()?.carId
    }

    override suspend fun addRecharge(recharge: Recharge) = execute {
        recharges.find(eq("carId", recharge.carId)).firstOrNull()?.run {
            throw InvalidInputException(CAR_ALREADY_IN_CHARGE)
        }
        recharges.insertOne(RechargeDbEntity(recharge.carId, recharge.chargingStationId)).let {}
    }


    override suspend fun deleteRecharge(recharge: Recharge) = execute {
        if (!recharges.deleteOne(and(
                eq("carId", recharge.carId),
                eq("chargingStationId", recharge.chargingStationId)
            )).wasAcknowledged()
        ) {
            throw InternalErrorException(OPERATION_FAILED_MESSAGE)
        }
    }

    override suspend fun startRecharge(recharge: Recharge) = execute {
        addRecharge(recharge)
    }

    override suspend fun stopRecharge(recharge: Recharge) = execute {
        recharges.find(and(
            eq("carId", recharge.carId),
            eq("chargingStationId", recharge.chargingStationId)
        )).firstOrNull() ?: throw NotFoundException(RECHARGE_NOT_FOUND_MESSAGE)
        deleteRecharge(recharge)
    }

    private suspend fun <T> execute(block: suspend () -> T): T =
        try {
            block()
        } catch (e: MongoException) {
            println("MongoDB exception: " + e.message)
            throw InternalErrorException(OPERATION_FAILED_MESSAGE)
        }
}
