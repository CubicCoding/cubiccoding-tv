package com.doepiccoding.cubiccodingtv.front.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.model.networking.calls.GroupsRequest
import com.doepiccoding.cubiccodingtv.model.networking.calls.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel: ViewModel() {

    private val login: MutableLiveData<Boolean> = MutableLiveData()
    private val allGroupsResponse: MutableLiveData<List<GroupsResponsePayload>?> = MutableLiveData()
    private val inProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    fun observeLogin(): LiveData<Boolean> = login
    fun observeAllGroups(): LiveData<List<GroupsResponsePayload>?> = allGroupsResponse
    fun getProgressState(): LiveData<Boolean> = inProgress

    fun login(username: String, password: String) {
        inProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginInfo = UserRequest.login(username, password)
                login.postValue(loginInfo)
                inProgress.postValue(false)
            } catch (e: Exception) {
                Timber.e(e, "Error during login...")
                login.postValue(false)
                inProgress.postValue(false)
            }
        }
    }

    fun getGroups() {
        inProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val groupsInfo = GroupsRequest.getAllGroups()
                allGroupsResponse.postValue(groupsInfo)
                inProgress.postValue(false)
            } catch (e: Exception) {
                Timber.e(e, "Error while getting the groups...")
                allGroupsResponse.postValue(null)
                inProgress.postValue(false)
            }
        }
    }
}
