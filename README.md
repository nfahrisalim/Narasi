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

## Screenshots
### *1 MAIN THEME*
![1 MAIN THEME](https://github.com/DissentPhase/Narasi/blob/main/README/1%20MAIN%20THEME.png)

### *2 READING VIEW*
![2 READING VIEW](https://github.com/DissentPhase/Narasi/blob/main/README/2%20READING%20VIEW.png)

### *3 COMMENT SCENE*
![3 COMMENT SCENE](https://github.com/DissentPhase/Narasi/blob/main/README/4%20COMMENT%20VIEW.png)

### *4 LOGIN VIEW*
![4 LOGIN VIEW](https://github.com/DissentPhase/Narasi/blob/main/README/5%20LOGIN%20VIEW.png)

### *5 REGISTER VIEW*
![5 REGISTER VIEW](https://github.com/DissentPhase/Narasi/blob/main/README/6%20REGISTER%20VIEW.png)

### *6 PUBLISH VIEW*
![6 PUBLISH VIEW](https://github.com/DissentPhase/Narasi/blob/main/README/7%20PUBLISH%20VIEW.png)

### *7 ADD CHAPTER*
![7 ADD CHAPTER](https://github.com/DissentPhase/Narasi/blob/main/README/7%20ADD%20CHAPTER.png)

### *8 INVALID EMAIL-ALERT*
![8 INVALID EMAIL-ALERT](https://github.com/DissentPhase/Narasi/blob/main/README/8%20INVALID-EMAIL%20ALERT.png)

### *9 INVALID PASS-ALERT*
![9 INVALID PASS-ALERT](https://github.com/DissentPhase/Narasi/blob/main/README/9%20INVALID-PASSWORD%20ALERT.png)

### *10 REGIST-SUCCS ALERT*
![10 REGIST-SUCCS ALERT](https://github.com/DissentPhase/Narasi/blob/main/README/10%20REGIST-SUCCS%20ALERT.png)

### *11 DELETE ALERT*
![11 DELETE ALERT](https://github.com/DissentPhase/Narasi/blob/main/README/11%20DELETE%20ALERT.png)

### *12 LOGOUT-ACC ALERT*
![12 LOGOUT-ACC ALERT](https://github.com/DissentPhase/Narasi/blob/main/README/12%20LOG-OUT%20ACC%20ALERT.png)

### *13 PUBLISH-ERROR WARNING*
![13 PUBLISH-ERROR WARNING](https://github.com/DissentPhase/Narasi/blob/main/README/13%20PUBLISH-ERROR%20WARNING.png)



## Pengujian Pada Aplikasi
![Table1](https://github.com/CSE-9124/TravelReview-project/blob/main/readme/Table1.png)
![Table2](https://github.com/CSE-9124/TravelReview-project/blob/main/readme/Table2.png)



     
 
