package com.doepiccoding.cubiccodingtv.front.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.Home
import com.doepiccoding.cubiccodingtv.front.splash.groups_recyclerview.GroupsAdapter
import com.doepiccoding.cubiccodingtv.front.utils.showFancyToast
import com.doepiccoding.cubiccodingtv.model.dtos.GroupsResponsePayload
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.splash_activity.*

class Splash: AppCompatActivity() {

    var model: SplashViewModel? = null
    var groupsAdapter: GroupsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        setupViewModel()
        setupViews()
    }

    private fun setupViewModel() {
        val model: SplashViewModel by viewModels()
        this.model = model

        model.observeLogin().observe(this, Observer { loginSuccess ->
            if (loginSuccess) {
                startGroupSelectionLogic()
            } else {
                showFancyToast(this, "Error al hacer login")
            }
        })

        model.getProgressState().observe(this, Observer { inProgress ->
            progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        })

        model.observeAllGroups().observe(this, Observer { groups ->
            groups?.let { populateGroups(it) } ?: nullGroupsResponse()
        })
    }

    private fun nullGroupsResponse() {
        showFancyToast(this, getString(R.string.error_downloading_groups))
    }

    private fun setupViews() {
        if (UserPersistedData.isLogged) {
            startGroupSelectionLogic()
        } else {
            prepareForLogin()
            loginButton.setOnClickListener {
                showFancyToast(this, "Logging In")
                model?.login(ccUsername.text.toString(), ccPassword.text.toString())
            }
        }

        //No matter what, prepare to populate the list of groups since we will definitely need it before moving to Home...
        groups.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        groupsAdapter = GroupsAdapter(this::groupClickCallback)
        groups.adapter = groupsAdapter
    }

    private fun prepareForLogin() {
        loginRelatedViews.visibility = View.VISIBLE
        bottomSplashHeader.text = getString(R.string.start_session)
        groups.visibility = View.GONE
        ccUsername.requestFocus()
    }

    private fun startGroupSelectionLogic() {
        loginRelatedViews.visibility = View.GONE//Make sure the login related views are out of the way...
        bottomSplashHeader.text = getString(R.string.select_a_group)
        model?.getGroups()
        showFancyToast(this, "Go fetch the groups...")
    }

    private fun populateGroups(groupsData: List<GroupsResponsePayload>) {
        groups.visibility = View.VISIBLE
        groupsAdapter?.setGroups(groupsData)
        groups.requestFocus()
    }

    private fun groupClickCallback(selectedGroup: GroupsResponsePayload) {
        showFancyToast(this, "Apuntando al aula: ${selectedGroup.classroomName}")
        UserPersistedData.classroomName = selectedGroup.classroomName ?: ""
        startActivity(Intent(this, Home::class.java))
    }
}
