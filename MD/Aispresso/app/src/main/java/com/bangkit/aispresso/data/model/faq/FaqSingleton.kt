package com.bangkit.aispresso.data.model.faq

object FaqSingleton {

    private val title = arrayOf(
        "Where can i watch",
        "Where can i watch",
        "Where can i watch",
        "Where can i watch",
        "Where can i watch",
    )

    private val subTitle = arrayOf(
        "Aispresso adalah aplikasi lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
        "Aispresso adalah aplikasi lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
        "Aispresso adalah aplikasi lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
        "Aispresso adalah aplikasi lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
        "Aispresso adalah aplikasi lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.",
    )


    val listProduk: ArrayList<FaqModel>
        get() {
            val list = arrayListOf<FaqModel>()
            for (position in title.indices){
                val faq = FaqModel()
                faq.title = title[position]
                faq.subTitle = subTitle[position]
                list.add(faq)

            }
            return list
        }
}