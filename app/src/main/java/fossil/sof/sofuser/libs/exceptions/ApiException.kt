package fossil.sof.sofuser.libs.exceptions

import fossil.sof.sofuser.libs.apierror.ApiError

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class ApiException(val apiError: ApiError) : RuntimeException()