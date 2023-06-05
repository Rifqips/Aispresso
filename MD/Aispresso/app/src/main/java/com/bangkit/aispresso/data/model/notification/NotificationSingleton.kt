package com.bangkit.aispresso.data.model.notification

object NotificationSingleton {

    private val title = arrayOf(
        "Temperatur saat ini adalah \n 22.85",
        "Temperatur saat ini adalah \n 24.45",
        "Temperatur saat ini adalah \n 25.31",
    )


    private val subtitle = arrayOf(
        "Pertahankan",
        "Pertahankan",
        "Pertahankan",
    )

    private val date = arrayOf(
        "1 day ago",
        "3 days ago",
        "4 days ago",
    )

    val listNotification: ArrayList<NotificationModel>
        get() {
            val list = arrayListOf<NotificationModel>()
            for (position in title.indices){
                val listNotif = NotificationModel()
                listNotif.title = title[position]
                listNotif.subTitle = subtitle[position]
                listNotif.date = date[position]
                list.add(listNotif)

            }
            return list
        }
}