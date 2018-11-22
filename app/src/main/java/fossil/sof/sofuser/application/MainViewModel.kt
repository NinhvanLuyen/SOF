package fossil.sof.sofuser.application

import fossil.sof.sofuser.libs.ActivityViewModel
import fossil.sof.sofuser.libs.Environment

interface MainViewModel {
    class ViewModel(environment: Environment) : ActivityViewModel() {
    }
}