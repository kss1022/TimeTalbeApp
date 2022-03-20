package com.example.suwon_university_community.data.entity.lecture

import androidx.annotation.StringRes
import com.example.suwon_university_community.R


enum class DepartmentCategory(
    @StringRes val categoryNameId: Int
){
    DS( R.string.data_sience),
    COM( R.string.computer),
    COMPUTER_SW(R.string.computer_sw),
    MEDIA_SW(R.string.media_sw),
    IF_CM(R.string.information_communication),
    IF_CM_SECURE(R.string.information_secure),
    IF_CM_COMMUNICATION(R.string.infromation_communication),
    CLOUD_CONVERGENCE(R.string.cloude_convergence)
}