package com.dicoding.edusafety.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportData(
    val category: String,
    val perpetratorName: String,
    val victimName: String,
    val incidentDate: String,
    val description: String,
    val image: Uri?
) : Parcelable