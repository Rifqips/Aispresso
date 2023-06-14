package com.bangkit.aispresso.data.model.faq

object FaqSingleton {

    private val title = arrayOf(
        "Tentang Aplikasi Aispresso?",
        "Apa itu Biji Kopi Longberry?",
        "Apa itu Biji Kopi Peaberry?",
        "Apa itu Biji Kopi Premium?",
        "Apa itu Biji Kopi Defect?",
    )

    private val subTitle = arrayOf(
        "AIspresso adalah aplikasi untuk mendeteksi kualitas biji kopi dengan metode pemrosesan gambar. selain itu Aispresso juga bisa mendeteksi penyakit pada tanaman.",

        "Biji kopi Longberry mengacu pada ukuran biji kopi yang lebih besar dari rata-rata. Istilah \"Longberry\" sering digunakan untuk varietas kopi Arabika. Karakteristik biji kopi Longberry antara lain:\n" +
                "1. Ukuran\nBiji kopi Longberry memiliki ukuran yang lebih besar dibandingkan dengan biji kopi standar. Diameter bijinya berkisar antara 7-8 mm.\n\n" +
                "2. Bentuk\nBiji kopi Longberry umumnya berbentuk oval dan memanjang, mirip dengan buah berry. Inilah alasan mengapa biji ini disebut \"Longberry\".\n\n" +
                "3. Rasa\nBiji kopi Longberry cenderung menghasilkan rasa yang lebih ringan dan lebih asam dibandingkan dengan biji kopi yang lebih kecil. Rasa yang dihasilkan dapat mencakup nuansa buah-buahan cerah dan keasaman yang cerdas.\n\n" +
                "4. Kualitas\nBiji kopi Longberry dianggap memiliki kualitas yang baik. Keberadaan biji kopi Longberry sering dihargai oleh pecinta kopi karena karakteristik rasa yang unik.\n",

        "Biji kopi Peaberry merujuk pada biji kopi yang terbentuk hanya satu butir dalam satu buah kopi, bukan dua butir seperti biasanya. Biasanya, sekitar 5-10% dari biji kopi yang dipanen adalah biji kopi Peaberry. Karakteristik biji kopi Peaberry antara lain:\n" +
                "1. Bentuk\nKarena hanya terdapat satu biji dalam buah kopi Peaberry, biji ini cenderung memiliki bentuk yang lebih bulat dan terlihat seperti biji kopi yang \"terpanggang\".\n\n" +
                "2. Rasa\nBiji kopi Peaberry sering dianggap memiliki rasa yang lebih konsisten dan lebih kompleks dibandingkan dengan biji kopi standar. Rasa yang dihasilkan dapat mencakup keasaman yang cerah, kelembutan, dan keharmonisan yang baik antara aroma dan rasa.\n\n" +
                "3. Kualitas\nBiji kopi Peaberry dianggap sebagai varietas khusus dan biasanya memiliki harga yang lebih tinggi dibandingkan dengan biji kopi standar. Hal ini karena produksi biji kopi Peaberry lebih langka dan membutuhkan pemisahan khusus dalam proses pengolahan kopi.",

        "Istilah \"Premium\" pada biji kopi mengacu pada kualitas yang lebih tinggi daripada biji kopi biasa. Namun, penting untuk dicatat bahwa istilah \"Premium\" tidak memiliki definisi standar yang diakui secara universal dalam industri kopi. Karakteristik biji kopi Premium yang mungkin terkait antara lain:\n" +
                "1. Kualitas\nBiji kopi Premium sering kali dipilih berdasarkan kualitas yang lebih baik, seperti tingkat keasaman yang seimbang, kehalusan, atau keselarasan rasa yang lebih baik. Mereka dapat berasal dari daerah pertanian kopi yang terkenal atau menggunakan metode pengolahan yang inovatif.\n\n" +
                "2. Harga\nBiji kopi Premium seringkali memiliki harga yang lebih tinggi dibandingkan dengan biji kopi biasa. Hal ini disebabkan oleh upaya yang lebih besar dalam memilih biji kopi dengan kualitas terbaik dan memberikan perhatian ekstra dalam proses pengolahan kopi.",

        "Biji kopi Defect merujuk pada biji kopi yang mengalami cacat atau kerusakan, yang dapat terjadi selama proses pengolahan atau penyimpanan biji kopi. Karakteristik biji kopi Defect antara lain:\n" +
                "1. Cacat\nCacat pada biji kopi dapat berupa biji yang retak, terinfeksi hama, terkontaminasi oleh bahan asing, atau memiliki warna yang tidak biasa.\n\n" +
                "2. Rasa\nBiji kopi Defect cenderung memiliki rasa yang buruk dan kualitas yang rendah. Cacat pada biji kopi dapat mempengaruhi cita rasa dan aroma akhir kopi yang dihasilkan.\n\n" +
                "3. Pemisahan\nBiasanya, biji kopi Defect dipisahkan dan dihindari dalam proses pemrosesan kopi. Hal ini dilakukan untuk menjaga kualitas keseluruhan dari hasil akhir dan memastikan hanya biji kopi yang berkualitas baik yang digunakan.",
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