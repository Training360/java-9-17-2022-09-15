class: inverse, center, middle

# Java 11 újdonságok

---

## Tematika

* Nyelvi módosítások
  * `var` használata lambda kifejezésekben
* API módosítások
* HTTP Client API
* JVM újdonságok  
  * Z és Epsilon Garbage Collector
* Tool
  * Java forráskód azonnali futtatása  (shebang)

---

class: inverse, center, middle

# Java 11 újdonságok - Bevezetés

---

## Java 11

* 2018\. szeptember
* Java 8 után az első LTS
* Oracle JDK production környezetben ingyenesen nem használható, <br /> fejlesztésre, oktatásra igen
* Open JDK használható
* [Alternatívák](https://en.wikipedia.org/wiki/OpenJDK):
  * AdoptOpenJDK -> Eclipse [Adoptium](https://adoptium.net/)
  * Amazon Corretto
  * Azul Zulu


https://en.wikipedia.org/wiki/Java_version_history

https://www.oracle.com/java/technologies/javase/11-relnote-issues.html

---

## Java 11 újdonságok

* `var` használata lambda kifejezésekben - JEP 323
* HTTP Client - JEP 321
* Nest-Based Access Control - JEP 181
* Java EE és CORBA eltávolítása - JEP 320
* Applets, Java Web Start eltávolítása
* Nashorn deprecated - JEP 335
* TLS 1.3 részleges támogatás - JEP 332
* Curve25519 és Curve448 algoritmusok - JEP 324
* ChaCha20 and Poly1305 Cryptographic Algorithms - JEP 329
* Azonnali futtatás - JEP 330
* Pack200 és Unpack200 deprecated - JEP 336

---

## Java 11 újdonságok

* Z GC - JEP 333
* Epsilon Garbage Collector - JEP 318
* Unicode 10 - JEP 327

---

class: inverse, center, middle

# Nyelvi újdonságok

---

## A var használata lambda <br /> kifejezésekben

```java
(s1, s2) -> s1 + s2
```

```java
(var s1, var s2) -> s1 + s2
```

```java
(@Nonnull var s1, @Nullable var s2) -> s1 + s2
```

(type annotation)

---
class: inverse, center, middle

# API módosítások

---

## String metódusok

* `isBlank()`, `lines()`, `repeat(int)`
* `strip()`, `stripLeading()`, `stripTrailing()`: Unicode aware

---

## Collection.toArray()

```java
List<String> names = List.of("John Doe", "Jack Doe", "Jane Doe");
//String[] arr = names.toArray(new String[] {});
String[] arr = names.toArray(String[]::new);
``` 

---

## Stream módosítások

* `Predicate` `not(Predicate)`
* `Optional*` `isEmpty()`
* `Pattern` `asMatchPredicate()`

```java
public Predicate<String> asMatchPredicate() {
    return s -> matcher(s).matches();
}
```

---
class: inverse, center, middle

# Fájlkezelés

---

## Fájlkezelés

* Files metódusok: `readString()`, `writeString()`, UTF-8
* `FileReader`, `FileWriter` `Charset` paraméterek bevezetése
* `Path.of` metódus a `Paths.get()` helyett

---
class: inverse, center, middle

# HTTP Client API

---

## Oldal letöltése

```java
var client = HttpClient.newHttpClient();
var request = HttpRequest.newBuilder()
        .uri(URI.create(URL_PREFIX))
        .build();
client.send(request, HttpResponse.BodyHandlers.ofLines())
        .body().forEach(System.out::println);
```

---

## HttpClient builderrel

```java
var client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();
```

---

## Aszinkron módon

```java
var client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_PREFIX))
        .build();
client.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
        .thenApply(HttpResponse::body)
        .thenApply(this::processLines)
        .join();
```

---
class: inverse, center, middle

# Nest-Based Access Control

---

## Nest-Based Access Control

* Fordítóprogramban bonyolult annak kezelése, hogy a belső osztályok <br /> hogyan férnek hozzá a tartalmazó osztály tagjaihoz
* Mivel külön állományba fordul, olyan bridge szintetikus metódusok <br /> kerülnek legenerálásra, melyek láthatósága bővebb
* Ennek változtatása, hogy ne legyen erre szükség
* Új fogalom bevezetése: nest, nest mates
* Következménye: változó Reflection API

.small-code-14[
```java
MainClass.class.isNestmateOf(MainClass.NestedClass.class) 
        // true
MainClass.NestedClass.class.getNestHost() 
        // MainClass.class
MainClass.NestedClass.class.getNestMembers() 
        // Class<?>[]{MainClass.class, MainClass.NestedClass.class}
```
]

---
class: inverse, center, middle

# További API módosítások

---

## JAXB és JAX-WS

Java EE és CORBA modulok eltávolítása: JAXB, JAX-WS

```java
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.4</version>
</dependency>
```

---

## Applets, Java Web Start

* Java Applets már nem elérhető
* Java Web Start már nem elérhető
* JWS alternatíva: IcedTea alternatív disztribúció IcedTea-Web projektje <br /> egy alternatív JWS implementáció

---

## JavaFX és Nashorn

* JavaFX már nem része
* Nashorn deprecated

---

## Security módosítások

* TLS 1.3 részleges támogatás
* Curve25519 és Curve448 kulcsegyeztető algoritmusok: kevésbé támadhatóak
* ChaCha20 és Poly1305 kriptográfiai algoritmusok megvalósítása

---
class: inverse, center, middle

# Tools

---

## Java forráskód azonnali <br /> futtatása

```shell
java HelloWorld.java
```

---

## Shebang

* Ne legyen a kiterjesztése `.java`, helyette `HelloWorld.sh`

```bash
#!/opt/java/jdk-11/bin/java --source 11

public class HelloWorld
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
    }
}
```

---

## Pack200 és Unpack200

* Pack200 és Unpack200 eltávolítása

---
class: inverse, center, middle

# JVM újdonságok

---

## Z Garbage Collector

* 11-ben még csak kísérleti, 15-ben lesz production ready
* Terabyte nagyságú heap-ek kezelésére
* 10 ms alatt a megállások
* Skálázható, memóriaterület nagyságának növekedésével ne nőjön a GC idő
* Max 15%-ot használjon a futási időből
* Felkészülés a multi-tiered heapre <br /> (flash és non-volatile memory elterjedésével, <br /> Non-volatile random-access memory (NV-RAM))
* Színes pointerek
* Load barrier

```shell
XX:+UnlockExperimentalVMOptions -XX:+UseZGC
```

https://wiki.openjdk.java.net/display/zgc/Main

---

## Epsilon Garbage Collector

* NOP
* Felhasználási területei: benchmark, memóriaműveletek tesztelésére, <br /> apró (parancsori) programoknál, VM teszteléshez

```shell
-XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
```

---

## Unicode

* Unicode 10.0.0 támogatás (új szkriptek és karakterek)
    * Szkript: betűk és írásjelek csoportja