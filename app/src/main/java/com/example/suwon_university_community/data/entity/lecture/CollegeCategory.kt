package com.example.suwon_university_community.data.entity.lecture

import androidx.annotation.StringRes
import com.example.suwon_university_community.R

// 경성대학 , ICT융합대학...

enum class CollegeCategory(
    @StringRes val categoryNameId: Int
) {
    ALL(R.string.all),
    ELECTIVE(R.string.elective),
    INTERNATIONAL(R.string.international),
    HUMANITIES_SOCIAL(R.string.humanities_social),
    BUSINESS_ECONOMY(R.string.business_economy),
    INDUSTRIES(R.string.industries),
    HEALTH_SCIENCE(R.string.health_science),
    ART(R.string.art),
    ICT(R.string.ict),
    MUSIC(R.string.music),
    FUSION_CULTURE_ARTS(R.string.fusion_culture_art),
}