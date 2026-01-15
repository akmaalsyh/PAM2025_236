package com.example.simkader_236.uicontroller.route
import com.example.simkader_236.R

object DestinasiDetail : DestinasiNavigasi {
    override val route = "item_details"
    override val titleRes = R.string.detail_kader_title
    const val kaderIdArg = "itemId"
    val routeWithArgs = "$route/{$kaderIdArg}"
}