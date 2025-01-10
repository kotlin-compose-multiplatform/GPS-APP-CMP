package com.gs.wialonlocal.features.monitoring.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gs.wialonlocal.core.constant.Constant
import com.gs.wialonlocal.core.network.Resource
import com.gs.wialonlocal.features.monitoring.data.entity.hardware.HardwareTypeEntity
import com.gs.wialonlocal.features.monitoring.data.entity.history.LoadEventRequest
import com.gs.wialonlocal.features.monitoring.data.entity.history.calculateTripStats
import com.gs.wialonlocal.features.monitoring.domain.model.getCurrentTimeInEpochSeconds
import com.gs.wialonlocal.features.monitoring.domain.usecase.MonitoringUseCase
import com.gs.wialonlocal.features.monitoring.presentation.state.AddressState
import com.gs.wialonlocal.features.monitoring.presentation.state.FieldState
import com.gs.wialonlocal.features.monitoring.presentation.state.HardwareTypeState
import com.gs.wialonlocal.features.monitoring.presentation.state.LoadEventState
import com.gs.wialonlocal.features.monitoring.presentation.state.LocatorState
import com.gs.wialonlocal.features.monitoring.presentation.state.ReportSettingsState
import com.gs.wialonlocal.features.monitoring.presentation.state.SummaryState
import com.gs.wialonlocal.features.monitoring.presentation.state.UnitState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitoringViewModel(
    private val useCase: MonitoringUseCase
) : ScreenModel {
    private val _units = MutableStateFlow(UnitState())
    val units = _units.asStateFlow()

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private var activeJobs = mutableListOf<Job>()

    private val _reportSettings = MutableStateFlow(ReportSettingsState())
    val reportSettings = _reportSettings.asStateFlow()

    private val _loadEventState = MutableStateFlow(LoadEventState())
    val loadEventState = _loadEventState.asStateFlow()

    private val _fieldState = MutableStateFlow(FieldState())
    val fieldState = _fieldState.asStateFlow()

    private val _summaryState = MutableStateFlow(SummaryState())
    val summaryState = _summaryState.asStateFlow()

    private val _locatorState = MutableStateFlow(LocatorState())
    val locatorState = _locatorState.asStateFlow()

    private val _hardwareTypes = MutableStateFlow(HardwareTypeState())
    val hardwareTypes = _hardwareTypes.asStateFlow()

    var addressState = mutableStateOf(AddressState())
        private set

    init {
        initHardwareTypes()
    }

    fun initHardwareTypes() {
        if (_hardwareTypes.value.types.isNullOrEmpty()) {
            getHardwareTypes()
        }
    }

    fun refreshUnit(id: String) {
        _units.value = _units.value.copy(
            data = _units.value.data?.map {
                if (it.id == id) {
                    it.copy(
                        lastOnlineTime = getCurrentTimeInEpochSeconds().toString()
                    )
                } else {
                    it
                }
            }
        )
    }

    fun getAddress(latitude: Double?, longitude: Double?, id: String) {
        // Cancel oldest job if more than 10 are active
        if (activeJobs.size >= 10) {
            activeJobs.firstOrNull()?.cancel()
            activeJobs.removeAt(0)
        }
        val job = viewModelScope.launch {
            useCase.getAddress(latitude ?: 0.0, longitude ?: 0.0).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        addressState.value = addressState.value.copy(
                            loading = false,
                            error = result.message,
                            data = result.data ?: ""
                        )
                    }

                    is Resource.Loading -> {
                        addressState.value = addressState.value.copy(
                            loading = true,
                            error = result.message,
                            data = null
                        )
                    }

                    is Resource.Success -> {
                        result.data?.let { address ->
                            _units.value = _units.value.copy(
                                data = _units.value.data?.map { d ->
                                    if (d.id == id) {
                                        d.copy(
                                            address = address
                                        )
                                    } else
                                        d
                                }
                            )
                        }
                        addressState.value = addressState.value.copy(
                            loading = false,
                            error = result.message,
                            data = result.data ?: ""
                        )
                    }
                }
            }.launchIn(this)
        }

        activeJobs.add(job)

        // Remove completed jobs
        job.invokeOnCompletion { activeJobs.remove(job) }
    }


    fun findHardwareType(id: Int?): HardwareTypeEntity? {
        return _hardwareTypes.value.types?.find { it.id == id }
    }

    fun getHardwareTypes() {
        screenModelScope.launch {
            useCase.getHardwareTypes().onEach {
                when (it) {
                    is Resource.Error -> {
                        _hardwareTypes.value = _hardwareTypes.value.copy(
                            loading = false,
                            error = it.message,
                            types = it.data
                        )
                    }

                    is Resource.Loading -> {
                        _hardwareTypes.value = _hardwareTypes.value.copy(
                            loading = true,
                            error = it.message,
                            types = it.data
                        )
                    }

                    is Resource.Success -> {
                        _hardwareTypes.value = _hardwareTypes.value.copy(
                            loading = false,
                            error = it.message,
                            types = it.data
                        )
                    }
                }
            }.launchIn(this)

        }
    }


    fun getLocatorUrl(duration: Long, items: List<String>, onSuccess: (String) -> Unit) {
        screenModelScope.launch {
            useCase.getLocatorUrl(duration, items).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _locatorState.value = _locatorState.value.copy(
                            loading = false,
                            error = result.message,
                            result = result.data
                        )
                    }

                    is Resource.Loading -> {
                        _locatorState.value = _locatorState.value.copy(
                            loading = true,
                            error = result.message,
                            result = result.data
                        )
                    }

                    is Resource.Success -> {
                        result.data?.let { data ->
                            onSuccess("https://gps.ytm.tm/locator/index.html?t=" + data.h)
                        }
                        _locatorState.value = _locatorState.value.copy(
                            loading = false,
                            error = result.message,
                            result = result.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }


    fun getFields(itemId: String) {
        screenModelScope.launch {
            useCase.getEvent(itemId).onEach {
                when (it) {
                    is Resource.Error -> {
                        _fieldState.value = _fieldState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                    }

                    is Resource.Loading -> {
                        _fieldState.value = _fieldState.value.copy(
                            loading = true,
                            error = it.message,
                            data = it.data
                        )
                    }

                    is Resource.Success -> {
                        _fieldState.value = _fieldState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                        getReportSettings(itemId)
                    }
                }

            }.launchIn(this)
        }
    }

    fun loadEvents(itemId: String, timeFrom: Long, timeTo: Long, onError: (String) -> Unit = {}) {
        screenModelScope.launch {
            useCase.loadEvents(
                req = LoadEventRequest(
                    itemId = itemId,
                    timeFrom = timeFrom,
                    timeTo = timeTo
                )
            ).onEach {
                when (it) {
                    is Resource.Error -> {
                        it.message?.let(onError)
                        _loadEventState.value = _loadEventState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                    }

                    is Resource.Loading -> {
                        _loadEventState.value = _loadEventState.value.copy(
                            loading = true,
                            error = it.message,
                            data = it.data
                        )
                    }

                    is Resource.Success -> {
                        println("___________CATEGORIZED_________________________")
                        println(it.data?.second)
                        println("___________CATEGORIZED_________________________")
                        _loadEventState.value = _loadEventState.value.copy(
                            loading = false,
                            error = it.message,
                            data = it.data
                        )
                        it.data?.second?.let { trips ->

                            _summaryState.value = _summaryState.value.copy(
                                tripMin = calculateTripStats(trips.filter { e -> e.type == "trip" }).first,
                                dayKm = calculateTripStats(trips).second,
                                parkingMin = calculateTripStats(trips.filter { e -> e.type == "park" }).first,
                            )
                        }

                        getFields(itemId)
                    }
                }
            }.launchIn(this)
        }
    }

    fun getReportSettings(itemId: String) {
        screenModelScope.launch {
            useCase.getReportSettings(itemId).onEach {
                when (it) {
                    is Resource.Error -> {
                        _reportSettings.value = _reportSettings.value.copy(
                            loading = false,
                            error = it.message,
                            settings = it.data
                        )
                    }

                    is Resource.Loading -> {
                        _reportSettings.value = _reportSettings.value.copy(
                            loading = true,
                            error = it.message,
                            settings = it.data
                        )
                    }

                    is Resource.Success -> {
                        _reportSettings.value = _reportSettings.value.copy(
                            loading = false,
                            error = it.message,
                            settings = it.data
                        )
                    }
                }
            }.launchIn(this)

        }
    }

    fun unloadEvents(id: String, onDone: () -> Unit = {}) {
        screenModelScope.launch {
            useCase.unloadEvents(id).onEach {
                when (it) {
                    is Resource.Error -> {
                        onDone()
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        onDone()
                    }
                }
            }.launchIn(this)
        }
    }

    fun startCheckUpdate() {
        screenModelScope.launch {
            while (true) {
                delay(5000L)
                _units.value.data?.let {
                    useCase.getUpdates(it).onEach { result ->
                        when (result) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                println("Check success")
                                _units.value = _units.value.copy(
                                    data = result.data
                                )
                            }
                        }
                    }.launchIn(this)
                }
            }

        }
    }

    fun getUnits(requireCheckUpdate: Boolean, isRequireAddress: Boolean = true) {
        screenModelScope.launch {
            useCase.getEvents(isRequireAddress).onEach {
                when (it) {
                    is Resource.Error -> {
                        _units.value = _units.value.copy(
                            loading = false,
                            error = it.message,
                            code = it.code,
                            data = it.data
                        )
                    }

                    is Resource.Loading -> {
                        _units.value = _units.value.copy(
                            loading = true,
                            error = it.message,
                            code = it.code,
                            data = it.data
                        )
                    }

                    is Resource.Success -> {
                        if (requireCheckUpdate) {
                            startCheckUpdate()
                        }
                        _units.value = _units.value.copy(
                            loading = false,
                            error = it.message,
                            code = it.code,
                            data = it.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun initUnits(requireCheckUpdate: Boolean, isRequireAddress: Boolean = true) {
        if (_units.value.data.isNullOrEmpty()) {
            getUnits(requireCheckUpdate, isRequireAddress)
        }
    }
}