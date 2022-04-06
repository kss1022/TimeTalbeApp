package com.example.suwon_university_community.data.api.response.lecture


import com.google.gson.annotations.SerializedName



data class LectureItem(
    @SerializedName("cplanYn")
    var cplanYn: String?,
    @SerializedName("cretEvalNm")
    var cretEvalNm: String?,
    @SerializedName("diclNo")
    var diclNo: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("estbDpmjCd")
    var estbDpmjCd: String?,
    @SerializedName("estbDpmjNm")
    var estbDpmjNm: String?,
    @SerializedName("estbMjorNm")
    var estbMjorNm: String?,
    @SerializedName("estbSubjtEnm")
    var estbSubjtEnm: String?,
    @SerializedName("estbSubjtNm")
    var estbSubjtNm: String?,
    @SerializedName("facDvcd")
    var facDvcd: String?,
    @SerializedName("facDvnm")
    var facDvnm: String?,
    @SerializedName("flangLtrRt")
    var flangLtrRt: Int?,
    @SerializedName("lssnLangCd")
    var lssnLangCd: String?,
    @SerializedName("ltrEmail")
    var ltrEmail: String?,
    @SerializedName("mpno")
    var mpno: String?,
    @SerializedName("orgClsCd")
    var orgClsCd: String?,
    @SerializedName("point")
    var point: Float?,
    @SerializedName("rcerYn")
    var rcerYn: String?,
    @SerializedName("reprPrfsEno")
    var reprPrfsEno: String?,
    @SerializedName("stafDeptNm")
    var stafDeptNm: String?,
    @SerializedName("stafNm")
    var stafNm: String?,
    @SerializedName("subjtCd")
    var subjtCd: String?,
    @SerializedName("subjtEstbSmrCd")
    var subjtEstbSmrCd: String?,
    @SerializedName("subjtEstbSmrNm")
    var subjtEstbSmrNm: String?,
    @SerializedName("subjtEstbYear")
    var subjtEstbYear: String?,
    @SerializedName("subjtNm")
    var subjtNm: String?,
    @SerializedName("theoPrac")
    var theoPrac: String?,
    @SerializedName("timtSmryCn")
    var timtSmryCn: String?,
    @SerializedName("trgtGrdeNm")
    var trgtGrdeNm: String?,
    @SerializedName("weekCplanYn")
    var weekCplanYn: String?,
    @SerializedName("xtsnNo")
    var xtsnNo: String?
)