class: inverse, center, middle

# Újdonságok Java 13-tól

---

## Tematika

* `DocumentBuilderFactory` Namespace aware metódusok, pl. `newDefaultNSInstance()`
* Socket API újraírása - JEP 353
* Dynamic CDS Archives - JEP 350

---

## Preview, incubator

* ZGC (csak Java 15-ben végleges) adja vissza az operációs rendszernek a nem használt memóriát JEP 351
* Text Blocks preview - Java 15-ben végleges - JEP 355
* Switch Expressions (preview) - Java 14-ben végleges - JEP 354

---

## `DocumentBuilderFactory` Namespace aware metódusok

---

## Socket API újraírása

* `net.Socket` és `java.net.ServerSocket` API modernebb, karbantarthatóbb implementációja
* Régi még JDK 1.0-ból, natív részeket tartalmaz
* Előkészítés a Project Loom fiber megvalósításának illesztésére
  * Új eszköz párhuzamos alkalmazások írására
  * user-mode thread
  * Javaban írt ütemező
  * Kisebb erőforrás, kevesebb memóriahasználat

---


## Dynamic CDS Archives

* Java 9-től classloader változás a Java Platform Module System miatt
  *  bootstrap class loader (null), `java.base` betöltéséért
  *  platform class loader (extension class loader lett átnevezve), standard osztálykönyvtár betöltéséért
  *  application class loader (más néven system class loader), alkalmazás osztályainak betöltéséért

* Java 10-től Application Class-Data Sharing (AppCDS)
  * Alkalmazás osztályai is bekerülhetnek
  * Hiszen a classloaderek innen is tudnak betölteni

* Java 13-tól nem kell újra létrehozni, hanem futás közben képes a még nem benne lévő osztályokat beletenni