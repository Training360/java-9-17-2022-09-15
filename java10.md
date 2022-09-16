class: inverse, center, middle

# Java 10 újdonságok

---

## Tematika

* Nyelvi módosítások
  * Local-Variable Type Inference
* Meglévő API-k
  * Unmodifiable collections
  * `Optional.orElseThrow()` metódus
* JVM változások
* Verziószámozás

---

class: inverse, center, middle

# Java 10 újdonságok - Bevezetés

---

## Módosítások

* 2018\. március
* Local-Variable Type Inference - JEP 286
* Unmodifiable collections
* `Optional.orElseThrow()` metódus
* Parallel Full GC for G1 - JEP 307
* Application Class-Data Sharing - JEP 310
* Experimental Java-Based JIT Compiler - JEP 317
* Heap Allocation on Alternative Memory Devices - JEP 316
* Verziószámozás - JEP 322

https://en.wikipedia.org/wiki/Java_version_history

https://www.oracle.com/java/technologies/javase/10-relnote-issues.html

---

class: inverse, center, middle

# Local-Variable Type Inference

---

## Egyszerű használat

```java
var message = "Hello, Java 10";
```

* Koncentráljunk a váltózónévre, ne a típusra
* Csak lokális változóknál, ahol van értékadás
* A `var` nem kulcsszó, hanem speciális típus, <br /> ezért használható változónévként (visszafele kompatibilitás)
* Nem lett tőle a Java dinamikus típusú nyelv, <br /> típus következtetés fordítási időben történik, <br /> és később nem változtatható

---

## Kerülendő

* Ha a kód olvashatatlanabb lesz tőle
* Pl. streameknél

```java
// Kerülendő!

var names = employees.stream()
    .filter(employee -> employee.getYearOfBirth() >= 2001)
    .map(Employee::getName)
    .map(String::toUpperCase);
```

---

## var kollekciók esetén

Klasszikus esetben ezt használjuk:

```java
List<String> names = new ArrayList<>();
```

Java 10-től

```java
var names = new ArrayList<String>();
```

Ennek felel meg:

```java
ArrayList<String> names = new ArrayList<String>();
```

---

## Lambda kifejezéseknél

```java
var one = (Supplier<Integer>) () -> 1;
System.out.println(one.get());
```

---

## Intersection types

.small-code-14[
```java
public interface LogReader {}
public interface LogStreamer {}
public class DummyLogReaderStreamer implements LogReader, LogStreamer {}
public class DummyDummyLogReaderStreamer implements LogReader, LogStreamer {}

public static void main(String[] args) {
    var l = pick(new DummyLogReaderStreamer(), 
                new DummyDummyLogReaderStreamer()); // LogReader & LogStreamer
}

static <T> T pick(T l1, T l2) {
    return l2;
}

public static <T extends LogReader & LogStreamer>void streamRead(T readerAndStreamer) {
    
}
```
]

---

class: inverse, center, middle

# Unmodifiable collections

---

## Unmodifiable collections

* `List.copyOf()`, `Set.copyOf()`, és `Map.copyOf()`
* `Collectors` `toUnmodifiableList()`, `toUnmodifiableSet()`, <br /> `toUnmodifiableMap()` metódusok
* A módosító metódusok `UnsupportedOperationException` kivételt dobnak
* Nem feltétlenül immutable (ha az elemei módosíthatóak)

---

class: inverse, center, middle

# Optional.orElseThrow()

---

## Optional.orElseThrow()

```java
public Employee findById(long id) {
    return findEmployeeById(id)
            .orElseThrow();
}

public Optional<Employee> findEmployeeById(long id) {
    return Optional.empty();
}
```

```java
public T orElseThrow() {
    if (value == null) {
        throw new NoSuchElementException("No value present");
    }
    return value;
}
```

---

class: inverse, center, middle

# JVM változások

---

## Konténer támogatás

* Linux rendszereken a JVM nem a host CPU számát és memóriáját adja vissza, hanem a konténerét

```shell
REM Kikapcsolás:
-XX:-UseContainerSupport
REM Processzor magok számának explicit megadása
-XX:ActiveProcessorCount=3
REM Java 8 update 191 óta
-XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=50 -XX:MinRAMPercentage=50
```

---

## Parallel Full GC for G1

* JDK 9 óta a G1 az alapértelmezett szemétgyűjtő, <br /> abban még single threaded mark-sweep-compact algorithm
* Full GC a parallel mark-sweep-compact algoritmust használja

---

## Application Class-Data Sharing

* JDK 5-ben jelent meg, hogy előfeldolgozott osztályok egy megosztott fájlból betölthetőek
  * Gyorsabb indulás
  * JVM-ek között megosztott használat, így kevesebb memóriafoglalás
  * Csak JDK osztályaihoz volt használható, mert csak a bootstrap classloader <br /> volt képes a használatára
* Application CDS (AppCDS) használatával az alkalmazás osztályai is használhatóak
  * Többi classloader is támogatja

---

## Kísérleti Java-alapú JIT fordító

* Graal egy Javaban írt dinamikus fordító 
* Használható JIT fordítóként
* 17-es Javaban eltávolították, mert nem használták, és <br />
  költséges volt a karbantartása
* Használható helyette a GraalVM

---

## Heap Allocation on Alternative <br /> Memory Devices

* Object heapet egy alternatív memóriaeszközön (pl.: NV-DIMM) foglalja le
  * NV - non-volatile
  * DIMM - dual in-line memory modul

---

class: inverse, center, middle

# Verziószámozás

---

## Verziószámozás

* Hathavonta új kiadás
* Long-term support verziószámában benne van az LTS, <br /> mely legalább 3 évig támogatott
* Feature release hat hónapig támogatott, a következő release-ig
* Java 11 LTS

```shell
$ java -version
openjdk version "10" 2018-03-20
OpenJDK Runtime Environment 18.3 (build 10+46)
OpenJDK 64-Bit Server VM 18.3 (build 10+46, mixed mode)
```

https://www.oracle.com/java/technologies/java-se-support-roadmap.html