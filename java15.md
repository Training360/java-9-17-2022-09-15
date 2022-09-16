class: inverse, center, middle

# Újdonságok Java 15-től

* Text Blocks - JEP 378
* `CharSequence.isEmpty()` metódus
* `NoSuchElementException` új konstruktorok
* Reimplement the Legacy DatagramSocket API - JEP 373
* Deprecate RMI Activation for Removal - JEP 385
* Hidden Classes - JEP 371
* ZGC - JEP 377
* Shenandoah: A Low-Pause-Time Garbage Collector - JEP 379
* Remove the Nashorn JavaScript Engine - JEP 372

## Preview, incubator

* Sealed Classes (Preview) - Java 17-ben végleges - JEP 360
* Pattern Matching for instanceof (Second Preview) - Java 16-ban végleges - JEP 375
* Records (Second Preview) - Java 16-ban végleges - JEP 384
* Foreign-Memory Access API (Second Incubator) - 17-ig nem véglegesített - JEP 383

## Text Blocks

```java
String text = """
            {
              "name": "John Doe",
              "age": 45,
              "address": "Doe Street, 23, Java Town"
            }
            """;
```

Megoldott problémák:

* Több soros konkatenáció, olvashatóság
* Idézőjelek escape-elése
* Copy-paste fejlesztőeszközből

## Speciális esetek

* Megtartja a behúzást, de a felesleges space-eket levágja

## Text Blocks

* IDEA támogatás
  * _Replace with text block_
  * Zölddel csíkkal jelöli, hogy mit vág le

---

## CharSequence.isEmpty()

* `interface CharSequence`
* Implementációi: `String`, `StringBuffer`, `StringBuilder`

---

## NoSuchElementException új konstruktorok

* `NoSuchElementException(String s, Throwable cause)`, 
* `NoSuchElementException(Throwable cause)`

---

# Reimplement the Legacy DatagramSocket API

* `java.net.DatagramSocket` és `java.net.MulticastSocket` API új implementációja
* Project Loom fiber megvalósításának illesztésére
* Java 1.0-ban került bevezetésre, C nyelvű kód
* Konkurrencia problémák, nehézkes IPv6 kezelés
* Régi `jdk.net.usePlainDatagramSocketImpl` visszakapcsolható

---

## Hidden Classes

* Javaban lehet olyan osztályokat létrehozni, amelyeknek nincs nevük, pl. anonymous inner classes
* JVM-nek limitációja volt, hogy minden osztálynak legyen neve
* Fordító generált hozzá nevet, dollárjellel
  * Javaban nem helyes osztálynév
  * JVM számára helyes osztálynév
* Ezeket is a classloader tölti be
  * Így mégis hozzá lehetett férni ezekhez
* Hidden classes JVM tulajdonság, mely nem engedi ezekhez az osztályokhoz való hozzáférést
* Alkalmazásfejlesztő számára nem használt tulajdonság
  * Fordítóprogramok, bytekódot módosító 3rd party library-k számára hasznos

---

## ZGC

* Terabyte nagyságú heap-ek kezelésére
* 10 ms alatt a megállások
* Skálázható, memóriaterület nagyságának növekedésével ne nőjön a GC idő
* Max 15%-ot használjon a futási időből
* Felkészülés a multi-tiered heapre <br /> (flash és non-volatile memory elterjedésével, <br /> Non-volatile random-access memory (NV-RAM))
* Színes pointerek
* Load barrier

---

## Shenandoah

* Concurrent, parallel
* Cél: magas reszponzivitás, rövid stop-the-world
* Cserébe: nagyobb processzorterhelés és memória
* Indirection pointer: objektum headerben lévő plusz adat, mely lehetővé
  teszi, hogy átmozgassuk az objektumot anélkül, hogy az összes 
  hivatkozást rá át kéne írni