package com.bangkit.aispresso.data.model.history.coffe

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ClassifyCoffeModel(
    var id: Int = 0,
    var classified: String? = null,
    var considers: String? = null,
    var image: ByteArray? = null,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassifyCoffeModel

        if (id != other.id) return false
        if (classified != other.classified) return false
        if (considers != other.considers) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (classified?.hashCode() ?: 0)
        result = 31 * result + (considers?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }

}