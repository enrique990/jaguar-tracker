package ni.edu.uam.jaguar_tracker.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import ni.edu.uam.jaguar_tracker.data.model.RoutineModel
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository

class HomeViewModel : ViewModel() {

    val routines: StateFlow<List<RoutineModel>> = RoutineRepository.routines
}