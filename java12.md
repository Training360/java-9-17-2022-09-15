class: inverse, center, middle

# Újdonságok Java 12-től

---

class: inverse, center, middle

# Java 12 újdonságok - Bevezetés

---

## Tematika

* API

---

## Java 12 megjelenése

* 2019\. március
* JEP: JDK Enhancement Proposal, OpenJDK és Oracle JDK specifikus javaslatok <br /> kezelésének folyamata
* JCP: Java Community Process, szervezet és folyamat a specifikációk kezelésére
* JSR: Java Specification Requests, a JCP-n belül fejlesztett specifikációk
* LTS - Long-term support
  * Többi is production-ready, azonban a következő verzióval megszűnik a támogatása

https://en.wikipedia.org/wiki/Java_version_history

https://www.oracle.com/java/technologies/javase/12-relnote-issues.html

https://wiki.openjdk.java.net/display/quality/Quality+Outreach

---

# Java 12 újdonságai

* API újdonságok
* JVM constants API - JEP 340
* Abortable Mixed Collections for G1 - JEP 344
* G1 gyakrabban tudja visszaadni az operációs rendszernek a nem használt memóriát - JEP 346
* Microbenchmark Suite - JEP 230
* Default CDS Archives - JEP 341

---

## Preview, incubator

* Switch (preview) - Java 14-ben végleges - JEP 325
* Shenandoah: A Low-Pause-Time Garbage Collector (Experimental) - Java 15-től - JEP 189

---

## IDEA beállítások

* `pom.xml`
* Settings / ... / Java Compiler (2 helyen)
* Project structure / Project és Modules (3 helyen)

---

# API újdonságok

* `String` `indent`
  * Negatív számmal is meghívható
  * Tab is whitespace, ami egynek számít
  * Normalizing line ending: kicseréli `\n`-re
* `String` `transform`

```java
String str = "Life's too short";
char[] result = str.transform(input -> input.concat(" to eat bad food"))
    .transform(String::toUpperCase) // String
    .transform(String::toCharArray); // char[]
```

---

## Mismatch

* `Files.mismatch(Path, Path)`
  * Két fájlt összehasonlít, és az első eltérő byte pozícióját adja vissza

---

* `Collectors` `teeing()`

---

* `NumberFormat` `getCompactNumberInstance()`

```java
NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
System.out.println(fmt.format(1000));
System.out.println(fmt.format(100000));
System.out.println(fmt.format(1000000));
```

```plaintext
1K
100K
1M
```

`NumberFormat.Style.LONG`

```plaintext
1 thousand
100 thousand
1 million
```

`new Locale("hu", "HU")`

```plaintext
1 E
100 E
1 M

1 ezer
100 ezer
1 millió
```

---

`InputStream.skipNBytes`

---

## CompletableFuture API

* CompletableFuture egy keretrendszer aszinkron feladatok létrehozására, <br /> kombinálására és futtatására
* Több, mint 50 metódus

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    try {
        TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
        throw new IllegalStateException(e);
    }
    return "Result of the asynchronous computation";
});
```

```java
class CompletableFuture<T> implements Future<T>, CompletionStage<T>
```

Új metódusok:

`CompletitionStage` `exceptionallyAsync`, `exceptionallyCompose`, `exceptionallyComposeAsync`

---

## JVM constants API

Hasznos:

* class fájlt módosító library-khez
* fordítási vagy linkelési időben futó statikus analízis eszközökhöz
* üzleti alkalmazásoknál nincs jelentősége

`class String implements Constable, ConstantDesc`

```java
 @Override
public Optional<String> describeConstable() {
    return Optional.of(this);
}

@Override
public String resolveConstantDesc(MethodHandles.Lookup lookup) {
    return this;
}
```

A `Constable` típus szerepelhet a Java class fájlba belefordítva, a constant pool területen.
Ez a JVMS 4.4-ben (Java Virtual Machine Specification) definiált.
A constant pool table bejegyzései loadable constantok. Az `ldc` és `invokedynamic` JVM
utasítások építenek ennek tartalmára. `String` és `Integer` típusokkal eddig sem volt
probléma, a `Class` típussal azonban igen.

Java 12-től már a `Class`, `MethodHandleDesc` és `MethodTypeDesc` kezelése is egyszerűbb.

---

* Abortable Mixed Collections for G1
  * Ha túl sokáig tart a szemétgyűjtés, átlép egy határt, abba tudja hagyni a teljes lefutás nélkül
* G1 gyakrabban tudja visszaadni az operációs rendszernek a nem használt memóriát
  * A G1 alapvetően Full GC-kor és concurrent gyűjtésnél adja vissza, melyek ritkák (ugyanis a G1 ügyel, hogy ne teljen meg a memória)
  * Beiktat concurrent gyűjtéseket (így hamarabb visszaadható az oprendszernek)

---

## Microbenchmark Suite

* Microbenchmarking nagyon nehéz
  * JVM belső optimalizációi miatt, pl. a JIT
* Van erre eszköz: [Java Microbenchmark Harness - JMH](https://openjdk.java.net/projects/code-tools/jmh/)
* Használata: https://www.baeldung.com/java-microbenchmark-harness
* A JEP tartalma, hogy a JDK forrásfájljai mellett legyenek a meglévő benchmarkok, valamint könnyebben
  lehessen ezeket futtatni, valamint újakat fejleszteni a JDK fejlesztőinek

---

## Default CDS Archives

* Több párhuzamosan futó JVM esetén csökkenti a memóriafoglalást és elindulás idejét
* JDK buildeléskor kerül legenerálásra, és a disztribúció része
  * Windows esetén `bin\server\classes.jsa`
* Második JVM indításakor az felhasználja az első által memóriába töltött archive-ot, mely gyorsabb, mint az
  osztálybetöltés