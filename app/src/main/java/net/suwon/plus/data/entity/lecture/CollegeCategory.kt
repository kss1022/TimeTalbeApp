package net.suwon.plus.data.entity.lecture

import androidx.annotation.StringRes
import net.suwon.plus.R


enum class CollegeCategory(
    @StringRes val categoryNameId: Int,
    val categoryTypeList: List<Pair<Int, List<Int>>>
) {

    ALL(R.string.all, listOf(R.string.all_type to emptyList())),


    ELECTIVE(
        R.string.elective,
        listOf(
            R.string.all_type to emptyList(),
            R.string.elective_type to emptyList(),
            R.string.elective_type_night to emptyList()
        )
    ),
    HUMANITIES_SOCIAL(
        R.string.humanities_social,
        listOf(
            R.string.all_type to emptyList(),
            R.string.humanities_social_type_human to listOf(
                R.string.humanities_social_type_kor,
                R.string.humanities_social_type_history
            ),
            R.string.humanities_social_foreign to listOf(
                R.string.humanities_social_eng,
                R.string.humanities_social_franc,
                R.string.humanities_social_russia,
                R.string.humanities_social_japan,
                R.string.humanities_social_china
            ),
            R.string.humanities_social_type_law_and_admin to listOf(
                R.string.humanities_social_type_law,
                R.string.humanities_social_type_admin
            ),
            R.string.humanities_social_type_media to listOf(),
            R.string.humanities_social_type_fire_night to listOf()
        )
    ),
    BUSINESS_ECONOMY(
        R.string.business_economy,
        listOf(
            R.string.all_type to listOf(),
            R.string.business_economy_type_manage_depart to listOf(
                R.string.business_economy_type_manage,
                R.string.business_economy_type_global,
                R.string.business_economy_type_account
            ),
            R.string.business_economy_type_economic to listOf(
                R.string.business_economy_type_economic_finace,
                R.string.business_economy_type_international_dev
            ),
            R.string.business_economy_type_hotel_tour to listOf(
                R.string.business_economy_type_hotel_manage,
                R.string.business_economy_type_hotel_eat_manage,
                R.string.business_economy_type_hotel_tour_manage
            )
        )
    ),
    INDUSTRIES(
        R.string.industries,
        listOf(
            R.string.all_type to listOf(),
            R.string.industries_type_construction_environmental_energy to listOf(
                R.string.industries_type_construction_environmental,
                R.string.industries_type_environmental_energy
            ),
            R.string.industries_type_architectural_city to listOf(
                R.string.industries_type_architectural,
                R.string.industries_type_city
            ),
            R.string.industries_type_industrial_machinery to listOf(
                R.string.industries_type_industry,
                R.string.industries_type_machinery
            ),
            R.string.industries_type_electronical_electronic to listOf(
                R.string.industries_type_electronical_materials,
                R.string.industries_type_electronical_physics
            ),
            R.string.industries_type_electronic_electron to listOf(
                R.string.industries_type_electronic,
                R.string.industries_type_electron
            ),
            R.string.industries_type_chemistry_new_material to listOf(
                R.string.industries_type_new_material,
                R.string.industries_type_chemistry
            ),
            R.string.industries_type_biochemistry to listOf(
                R.string.industries_type_bio_science,
                R.string.industries_type_bio_science_marketing,
                R.string.industries_type_fusion_chemistry
            ),
            R.string.industries_type_system_semiconductor to listOf()
        )
    ),
    HEALTH_SCIENCE(
        R.string.health_science,
        listOf(
            R.string.all_type to listOf(),
            R.string.health_science_type_nurse to listOf(),
            R.string.health_science_type_food to listOf(),
            R.string.health_science_type_sport_science to listOf(
                R.string.health_science_type_exercise_health,
                R.string.health_science_type_physical,
                R.string.health_science_type_leisure
            ),
            R.string.health_science_type_child to listOf(),
            R.string.health_science_type_clothing to listOf()
        )
    ),
    ART(
        R.string.art,
        listOf(
            R.string.all_type to listOf(),
            R.string.art_type_sculpture to listOf(),
            R.string.art_type_design to listOf(
                R.string.art_type_craft,
                R.string.art_type_communication
            )
        )
    ),
    ICT(
        R.string.ict,
        listOf(
            R.string.all_type to listOf(),
            R.string.ict_type_com to listOf(R.string.ict_type_com_sw, R.string.ict_type_media_sw),
            R.string.ict_type_info to listOf(
                R.string.ict_type_info_communication,
                R.string.ict_type_protection
            ),
            R.string.ict_type_data to listOf(),
            R.string.ict_type_cloud_fusion to listOf()
        )
    ),
    MUSIC(
        R.string.music,
        listOf(
            R.string.all_type to listOf(),
            R.string.music_type_compos to listOf(),
            R.string.music_type_voice to listOf(),
            R.string.music_type_piano to listOf(),
            R.string.music_type_instrument to listOf(),
            R.string.music_type_kor to listOf()
        )
    ),
    INTERNATIONAL(
        R.string.international,
        listOf(
            R.string.all_type to listOf(),
            R.string.international_type_free_major to listOf()
        )
    ),
    FUSION_CULTURE_ARTS(
        R.string.fusion_culture_art,
        listOf(
            R.string.all_type to listOf(),
            R.string.fusion_culture_art_type_culture to listOf(
                R.string.fusion_culture_art_type_movie,
                R.string.fusion_culture_art_type_drama,
                R.string.fusion_culture_art_type_dance,
                R.string.fusion_culture_art_type_culture_content
            )
        )
    ),
}