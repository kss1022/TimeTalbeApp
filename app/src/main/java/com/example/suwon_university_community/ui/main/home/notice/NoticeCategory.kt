package com.example.suwon_university_community.ui.main.home.notice

import androidx.annotation.StringRes
import com.example.suwon_university_community.R

enum class NoticeCategory(
    @StringRes   val categoryNameId: Int,
){
    ALL(R.string.all_notice),
    INTEGRATION(R.string.integration_notice),
    DEPARTMENT(R.string.department_notice),
    EMPLOYMENT(R.string.employment_notice),
    STARTUP(R.string.startup_notice),
    INTERNATIONAL_COOPERATION(R.string.international_cooperation_notice),
    LANGUAGE_SCHOOL(R.string.language_school_notice),
    SCHOLARSHIP_AND_LOAN(R.string.scholarship_and_loan_notice)
}