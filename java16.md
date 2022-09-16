class: inverse, center, middle

# Újdonságok Java 16-tól

---

* Kiadás: 2021. március

---

* Pattern Matching for instanceof - JEP 394
* Records - JEP 395
* Day period support
* `Stream.mapMulti()` és primitív típusos társai
* `Objects.checkFromIndexSize`, `Objects.checkFromToIndex`, `Objects.checkIndex()`
* `IndexOutOfBoundsException(long index)` konstruktor
* Enable C++14 Language Features - JEP 347
* Unix-Domain Socket Channels - JEP 380
* Strongly Encapsulate JDK Internals by Default - JEP 396
* Elastic Metaspace - JEP 387
* ZGC: Concurrent Thread-Stack Processing - JEP 376
* Warnings for Value-Based Classes - JEP 390
* Packaging Tool - JEP 392
* Migrate from Mercurial to Git - JEP 357
* Migrate to GitHub - JEP 369

---

## Preview, incubator

* Vector API (Incubator) - Java 17-ig nem végeleges - JEP 338
* Sealed Classes (Second Preview) - Java 17-ben végleges - JEP 397
* JEP 393: Foreign-Memory Access API (Third Incubator)
* Foreign Linker API (Incubator) - Java 17-ig nem végleges - JEP 389

---

## Pattern Matching for instanceof 

```java
Object o = new GrapeClass(Color.BLUE, 2);
if (o instanceof GrapeClass) {
    GrapeClass grape = (GrapeClass) o;
    System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
}
```

```java
Object o = new GrapeClass(Color.BLUE, 2);
if (o instanceof GrapeClass grape) {
    System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
}
```

* Változó scope-ja:

```java
Object o = new GrapeClass(Color.BLUE, 2);
if (o instanceof GrapeClass grape && grape.getNbrOfPits() == 2) {
    System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
}
```

* A `||` esetén nem fog lefordulni

```java
Object o = new GrapeClass(Color.BLUE, 2);
if (!(o instanceof GrapeClass grape)) {
    throw new RuntimeException("Not instance of GrapeClass");
}
System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
```

---

## Records

* Immutable osztályokhoz
* konstruktor, getterek, `hashCode`, `equals` és `toString` metódusok
  * Fejlesztőeszközzel generálva
  * Lombok (`@Value` annotáció)


```java
public record GrapeRecord(Color color, int nbrOfPits) {
}
```

* Őse: `java.lang.Record`
* Metódusok override-olhatóak

Konstruktorral:

```java
record GrapeRecord(String color, int nbrOfPits) {
    GrapeRecord {
        System.out.println("Parameter color=" + color + ", Field color=" + this.color());
        System.out.println("Parameter nbrOfPits=" + nbrOfPits + ", Field nbrOfPits=" + this.nbrOfPits());
        if (color == null) {
            throw new IllegalArgumentException("Color may not be null");
        }
    }
}
```

```java
GrapeRecord grape = new GrapeRecord("y", 12);
System.out.println(grape.color());
```

* Attribútumok értékadása a konstruktor lefutása után
  * Konstruktor törzsében nem adható neki érték

* IDEA támogatás: Class can be a record

* Streameknél megy

`grapes.map(GrapeRecord::color)`

* Spring + Jackson + Lombok (`@Value`) bonyolult (https://stackoverflow.com/questions/39381474/cant-make-jackson-and-lombok-work-together)
* Jackson alapértelmezetten támogatta
* ModelMapper Exception: nem talált paraméter nélküli konstruktort
* JPA
  * Entitás nem lehet
  * `@Embeddable` nem lehet
  * Egy mezőbe menthető: `AttributeConverter`
  * DTO-ként: Projection query

## Record működése

* `ObjectMethods` osztály
* Bootstrap metódusokat tartalmaz
* Ezeket használhatja a Java compiler, a record metódusainak törzsének implementálásához
* `MethodType` és `MethodHandle` típusok

---

## MethodHandle

* Java 7-ben jelent meg
* Alacsony szintű típusos, futtatható referencia metódusra, konstruktorra, <br /> attribútumokra, stb. (Képes paraméter és visszatérési érték transzformációra)
* Reflection egy modernebb megvalósításaként gondolhatunk rá, <br /> de nehezebb használni
* Erre épít az _invokedynamic_, mely a dinamikus nyelvek alapja (pl. Ruby), <br /> és használatos a lambda kifejezések futtatásakor <br />(nem készít anonymous inner classt)

---

## Példa a MethodHandle <br /> használatára

```java
Employee employee = new Employee("John Doe", 1980);

MethodType mt = MethodType.methodType(String.class); // Visszatérési típus
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle mh = 
    lookup.findVirtual(Employee.class, "getName", mt); // Osztály, metódusnév

String name = (String) mh.invoke(employee);
```

## Day Period Support 

```java
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("B");
System.out.println(dtf.format(LocalTime.of(8, 0)));
System.out.println(dtf.format(LocalTime.of(13, 0)));
System.out.println(dtf.format(LocalTime.of(20, 0)));
System.out.println(dtf.format(LocalTime.of(23, 0)));
System.out.println(dtf.format(LocalTime.of(0, 0)));
```

```plaintext
in the morning
in the afternoon
in the evening
at night
midnight
```

```java
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("B").withLocale(new Locale("hu", "HU"));
```

```plaintext
reggel
délután
este
éjjel
éjfél
```

## DateTimeFormatterBuilder

* Pattern helyett kódból állíthatjuk elő a formattert

```java
DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .optionalStart()
        .appendPattern(".SSS")
        .optionalEnd()
        .optionalStart()
        .appendZoneOrOffsetId()
        .optionalEnd()
        .optionalStart()
        .appendOffset("+HHMM", "0000")
        .optionalEnd()
        .toFormatter();
```

Új metódus: `DateTimeFormatBuilder.appendDayPeriodText()`

## Stream.mapMulti()

* Hasonló a `flatMap()` metódushoz

```java
<R> Stream<R> mapMulti​(BiConsumer<T, Consumer<R>> mapper)
```

```java
List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
double percentage = .01;
List<Double> evenDoubles = integers.stream()
  .<Double>mapMulti((integer, consumer) -> {
    if (integer % 2 == 0) {
        consumer.accept((double) integer * ( 1 + percentage));
    }
  })
  .collect(toList());
```

* one-to-zero or one-to-one transformation

https://www.baeldung.com/java-mapmulti

```java
List<Pair<String, String>> artistAlbum = albums.stream()
  .<Pair<String, String>> mapMulti((album, consumer) -> {
      for (Artist artist : album.getArtists()) {
          consumer.accept(new ImmutablePair<String, String>(artist.getName(), album.getAlbumName()));
      }
  })
```

* one-to-many

Mikor használjuk?

* Ha 0 v. kevés elemmé akarjuk konvertálni, a `flatMap`-nél hatékonyabb, mert ott mindig streammé kell alakítani
* Ha egyszerűbb előállítani az elemeket magukban, mint elemek streamjét előállítani

## Unix-Domain Socket Channels

* `SocketChannel` és `ServerSocketChannel` API támogatja a Unix-Domain Socketek használatát
* Inter-process communication (IPC) on same physical host
* Kezdetben Unix, de már Windowson is elérhető
* Mikor használjuk loopback ipcím és port helyett?
  * Sebesség, hiszen nem megy át a TCP/IP stacken, azaz több hálózati rétegen
  * Biztonság
* Pl. Docker serverrel való kommunikációra

## Elastic Metaspace

* Metaspace: permgen helyett, legnagyobb különbség: automatikusan nőhet
  * GC a nem használt osztályokat törli

Változtatás:

* Gyorsabban adja vissza a nem felhasznált területet az operációs rendszernek
* Metaspace mérete legyen kisebb
* Kódegyszerűsítés a könnyebb karbantarthatóságért

## Warnings for Value-Based Classes

* Value-based Classes:
  * `final` és immutable
  * `equals`, `hashCode`, és `toString` metódusok az állapota alapján
  * nincs konstruktoruk, csak gyártó metódusok

Ahol az egyik objektumot használhatom, ugyanott használhatom az ugyanazzal az állapottal rendelkező
másik objektumot is.

`@jdk.internal.ValueBased` annotáció

* Csomagoló osztályok - konstruktorok ezért depracated-ek
* `java.time` osztályai
* `Optional`

Továbblépés a Valhalla Project felé, primitive class bevezetésével.
Hatékonyabban ábrázolhatóak a memóriában, büntetlenül másolhatóak.



## Packaging Tool

```shell
jpackage --input target --name LocationsCli --main-jar locations.jar --type exe --win-console --app-version 1.0.0
```

```shell
Can not find WiX tools (light.exe, candle.exe)                                                  
Download WiX 3.0 or later from https://wixtoolset.org and add it to the PATH.
Error: Invalid or unsupported type: [exe] 
```

