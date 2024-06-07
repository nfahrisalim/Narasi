# Narasi

## Group Name : Cicfo All-Star
Nama Anggota:

  1. H071231031 – Muh. Naufal Fahri Salim
  2. H071231057 – Reynaldy Arungla’bi’
  3. H071231045 – Muh. Aipun Pratama

## Tema yang dipilih : Books, Reference, and Education

## Nama Pendamping : Muh. Resky Fadil

## Tim Juri :
  1. Ketua Umum BEM FMIPA
  2. Ahmad Hamsa Pattuner

## Executive Summary :

### Masalah:
Mahasiswa sering kali mengalami kesulitan dalam menemukan platform 
yang tepat untuk mengekspresikan dan membagikan karya tulis mereka, 
seperti cerpen, novel, dan puisi. Kendala ini termasuk keterbatasan
dalam manajemen karya, kurangnya visibilitas, serta minimnya interaksi 
dan umpan balik dari pembaca.

### Latar Belakang:
Di era digital ini, kemampuan untuk mempublikasikan dan
mendistribusikan karya secara online menjadi semakin penting. Banyak 
penulis muda mencari platform yang tidak hanya mendukung publikasi 
karya mereka, tetapi juga menyediakan fitur-fitur yang mempermudah 
pengelolaan, pencarian, dan interaksi dengan audiens. Archive of Our 
Own (AO3) adalah contoh sukses dari platform yang memenuhi 
kebutuhan tersebut di kalangan penggemar fiksi. Namun, kebutuhan 
yang serupa juga ada di kalangan mahasiswa yang ingin berkarya 
dalam bentuk cerpen, novel, dan puisi.

### Alasan Pemilihan Proyek:
Proyek ini dipilih karena menyediakan solusi komprehensif bagi 
mahasiswa yang ingin mengembangkan dan membagikan karya tulis 
mereka. Dengan mengadopsi beberapa fitur utama dari platform seperti 
AO3, proyek ini dapat memberikan pengalaman yang menyeluruh dan 
memuaskan bagi penulis muda. Fitur-fitur ini termasuk sistem pencarian 
dan filter lanjutan, manajemen tag, pemberian peringatan konten, serta 
fasilitas untuk interaksi antara penulis dan pembaca.

## Fitur Aplikasi :
A. General 
  1. *Login User*
  2. *Register User*
  3. *Search*
  4. *Logout User*
  5. *Jenis karya & Genre*

B. User
  1. Memiliki fitur untuk membaca karya secara anonymous ( tanpa login ) atau dengan login yang dapat dilakukan dengan pencarian, membuka karya yang tersedia.
  2. Manajemen Karya Pengguna yang dapat mengatur penggunaan dan kemudahan pengaturan dengan sistem tagging dan fitur draaft
  3. Memiliki fitur interaksi seperti like dan comment yang memungkinkan karya dan interaksinya dapat terlihat oleh pembaca maupun publisher
  4. Visibilitas dan Eksplorasi dari sistem pencarian serta filter karya yang memudahkan user dalam mencari dan memilih karya yang diingikan

## Penjelasan Penerapan Prinsip OOP :
1. Kelas `MainView` mengimplementasikan UI untuk "Narasi -Platform Karya Tulis Mahasiswa", menggunakan komponen UI untuk abstraksi, variabel `private` untuk enkapsulasi, mewarisi `Application` JavaFX untuk mengoverride `start`, dan menerapkan polimorfisme dengan `setOnAction` dan lambda expressions.
2. Kelas `LoginView` mengimplementasikan UI login dengan abstraksi melalui komponen UI, enkapsulasi dengan variabel `private`, dan polimorfisme dengan `setOnAction`. Kelas ini berinteraksi dengan `MainView` yang mewarisi `Application`.
3. Kelas `RegisterView` menggunakan abstraksi dengan komponen UI, enkapsulasi dengan variabel `private`, dan polimorfisme dengan `setOnAction`. Kelas ini berinteraksi dengan `LoginView`.
4. Kelas `AccountManage` menggunakan abstraksi dengan komponen UI, enkapsulasi dengan variabel `private`, dan polimorfisme dengan `setOnAction`. Kelas ini berinteraksi dengan model `Work`dan `DBManager`.
5. Kelas `ReadingView` menggunakan enkapsulasi melalui penggunaan variabel dan metode, serta pewarisan dalam kelas MainView yang mengganti perilaku metode bawaan. Polimorfisme ditunjukkan melalui metode startViewing() dan startReadingView() yang mengubah perilaku metode yang diwarisi.
   
## Mentoring
- Kak Muh. Resky Fadil - [24/5/2024]
- Kak Muh. Resky Fadil - [29/5/2024]
- Kak Muh. Resky Fadil - [31/5/2024]

## Link Repository GitHub
[https://github.com/DissenPhase/Narasi](https://github.com/DissentPhase/Narasi)





     
 
