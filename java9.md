class: inverse, center, middle

# Java 9 újdonságok

---

## Tematika

* Nyelvi módosítások
    * Privát metódusok interfészekben
    * Try-with-resources final/effectively final változókon
    * Diamond operátor anonymous inner class esetén
    * Aláhúzás karakter változónévként
    * `@SafeVarargs` annotáció
* Java Module System, jlink, Multi-release JARs

---

## Tematika

* Meglévő API-k
    * Collections Framework módosítások
    * Stream API módosítások
    * IO módosítások
    * Method handle
    * Process API, `ProcessHandle`
    * CompletableFuture API továbbfejlesztés
    * UTF-8 Properties fájl
    * `@Generated` annotáció megjelentése
    * `@Deprecated`
    
---

## Tematika

* Publish-Subscribe Framework - Reactive streams
* Stack-Walking API
* XML Catalog
* JVM módosítások
* Tool frissítések
* Ahead-of-Time Compilation
* JShell - Java REPL

---

class: inverse, center, middle

# Java 9 újdonságok - Bevezetés

---

## Java 9 megjelenése

* 2017\. szeptember
* JEP: JDK Enhancement Proposal, OpenJDK és Oracle JDK specifikus javaslatok <br /> kezelésének folyamata
* JCP: Java Community Process, szervezet és folyamat a specifikációk kezelésére
* JSR: Java Specification Requests, a JCP-n belül fejlesztett specifikációk
* 91 JEP

https://en.wikipedia.org/wiki/Java_version_history

https://openjdk.java.net/projects/jdk9/

https://docs.oracle.com/javase/9/whatsnew/toc.htm

---

## Java 9 újdonságai

* Apróbb nyelvi kiegészítések (Milling Project Coin)
  * Legismertebb: interfészekben privát metódusok - JEP 213
* Java Platform Module System (JPMS), korábban Project Jigsaw - JSR 376
    * Modularizált alkalmazások fejlesztésére, JDK is modularizált lett
    * Ehhez kapcsolódóan The Java Linker, összeállít egy futtatható csomagot <br /> JDK és alkalmazás
      megfelelő moduljaival - JEP 282
* API szinten továbblépés a funkcionális programozás felé
* Kollekciókhoz factory metódusok - JEP 269
* Variable handles - JEP 193
  * Reflection-szerű API bővült
  * `java.util.concurrent.atomic` <br /> és `sun.misc.Unsafe`-hez hasonló)
* UTF-8 Properties fájl - JEP 226
* `@Generated` annotáció megjelentése

---

## Java 9 újdonságai

* `@Deprecated` annotáció kiegészítése - JEP 277
* Deprecated: Applet - JEP 289, Java Plug-in
* Reactive Streams megjelenése, Process API továbbfejlesztés, <br /> apróbb párhuzamossági továbbfejlesztések - JEP 266
* Stack-Walking API - JEP 259
* XML Catalog standard API - JEP 268
* G1 legyen az alapértelmezett szemétgyűjtő - JEP 248
* Compact Strings - JEP 254
* Unified GC Logging - JEP 271
* Ahead-of-Time Compilation
  * JVM indítása előtt natív kóddá fordítás - JEP 295
* HTML 5, kereshető JavaDoc dokumentáció - JEP 224, JEP 225
* JShell (parancssoros REPL eszköz) - JEP 222

---

class: inverse, center, middle

# Java 9 nyelvi módosításai

---

## Privát metódusok interfészekben

* Statikus metódusok
* Példánymetódusok

---

## Statikus privát metódusok <br /> interfészekben

.small-code-14[
```java
public interface Printable {

    static Printable of(String csv) {
        String[] parts = csv.split(",");
        return create(parts[0], parts[1]);
    }

    static Printable of(Map<String, String> field) {
        return create(field.get("type"), field.get("title"));
    }

    private static Printable create(String type, String title) {
        // Létrehoz egy új példányt a típus alapján
    }
}
```
]

---

## Privát példánymetódusok <br /> interfészekben

.small-code-14[
```java
public interface Printable {

    String getTitle();

    default String getBody() {
        return "[no body]";
    }

    default String toStringRepresentation() {
        return annotate("title", getTitle()) +
                ", " +
                annotate("body", getBody());
    }

    private String annotate(String fieldName, String content) {
        return String.format("%s: %s", fieldName, content);
    }

}
```
]

---

## Try-with-resources deklarált változókon

```java
Scanner scanner = 
    new Scanner(ReadFile.class.getResourceAsStream("employees.txt"));
try (scanner) {
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
}
```

* `final` vagy _effectively final_ változókon

---

## Diamond operátor anonymous <br /> inner class esetén

.small-code-14[
```java
public interface Crate<T> {

    void put(T content);

    void empty();

}
```
]

.small-code-14[
```java
* Crate<String> crate = new Crate<>() {
    @Override
    public void put(String content) {
        System.out.printf("Content: %s\n", content);
    }

    @Override
    public void empty() {
        System.out.println("Empty");
    }
};
```
]

---

## Aláhúzás karakter

* Változónévként egy aláhúzás karakter (`_`) már nem fog lefordulni

---

class: inverse, center, middle

# Java 9 nyelvi módosításai - @SafeVarargs

---

## Háttér

* Visszafele kompatibilis a generikusok bevezetése: type erasure - típus paramétert Object típusra cserél
* Non-reifiable types: elveszik futásidőben a típusinformáció

```java
List<String>[] l = new List<String>[3];
```

* Nem fordul le: ˙`java: generic array creation`

```java
List<String>[] l = (List<String>[]) new List<?>[3];
```

* Warning: `java: unchecked cast`

---

## Heap pollution

* Ha futásidőben egy paraméteres típussal deklarált változó a nem neki 
megfelelő típusú objektumra tart referenciát

```java
List<String>[] l = (List<String>[]) new List<?>[3];  // Warning
Object[] objectArray = l;     // Valid
objectArray[1] = Arrays.asList(1, 2);
System.out.println(l[1].get(0));
```

* Futásidejű hiba: 

```plaintext
ClassCastException: class java.lang.Integer cannot 
be cast to class java.lang.String
```

---

## Varargs és generikus típus

```java
public static <T> List<T> asList(T... elements) {
    List<T> result = new ArrayList<>();
    for (T e : elements) {
        result.add(e);
    }
    return result;
}
```

```plaintext
Possible heap pollution from parameterized 
vararg type java.util.List<java.lang.String>
```

* Fordító által nehezen eldönthető, <br /> hogy megtörténik-e a heap pollution

---

## Működés

* Varargs ún. syntactic sugar
* Átalakítás

```java
asList("a", "b", "c");
```

```java
asList(new Object[]{"a", "b", "c"});
```

* Hiszen `new T[]`-re nem alakíthatja

---

## JDK példa

```java
static void m(List<String>... stringLists) {
*    Object[] array = stringLists; // 1
    List<Integer> tmpList = Arrays.asList(42);
    array[0] = tmpList; // Semantically invalid, but compiles without warnings
    String s = stringLists[0].get(0); // Oh no, ClassCastException at runtime!
}
```

* Nincs warning az 1-essel jelölt sorban!

---

## StackOverflow példa

.small-code-14[
```java
static <T> T[] asArray(T... args) {
  return args;
}

static <T> T[] arrayOfTwo(T a, T b) {
  return asArray(a, b); // 1
}

public static void main(String[] args) {
  String[] bar = arrayOfTwo("hi", "mom"); // 2
}
```
]

```plaintext
ClassCastException: class [Ljava.lang.Object; 
cannot be cast to class [Ljava.lang.String; 
([Ljava.lang.Object;
```

* 1-essel jelölt sorban egy `new Object[]{a, b}` van, <br /> ezt adja vissza, és ezt próbálja a 2-essel
  jelölt sor <br /> `String[]` objektummá alakítani

---

## Megoldás

* `@SafeVarargs` annotáció, `@SuppressWarnings` párja, elnyomjuk a Warningot
* Fejlesztő garantálja, hogy nem végez olyan műveletet, <br /> melyből később `ClassCastException` lehet
* A bejárással nincsen probléma
* Biztonságos: ha csak arra támaszkodunk, hogy az elemek típusa `T`
* Nem biztonságos: ha arra építünk, hogy a tömb típusa `T[]`

---

## JDK-ban

```java
@SafeVarargs
public static <T> List<T> asList(T... a) {
    return new ArrayList<>(a);
}
```

---

## Milyen metódusokra tehető?

* Nem tehető olyan metódusra, mely override-olható
* Rátehető `static` vagy `final` metódusra
* Java 9-től rátehető `private` metódusra is

---

class: inverse, center, middle

# Java Platform Module System

---

## Java Platform Module System kialakulásának okai

* Létező láthatósági mechanizmus: csomagok, nagyon korlátozott
* Maven modulok: csak fordítási időben, alapegysége a modul
* JAR hell: hiányzó JAR-ok, vagy ugyanannak több verziója
* OSGi: különálló szabvány, ez már a dinamikus betöltést is támogatja
  * Jelentős overhead a fejlesztésen

---

## Java Platform Module System

* Alapegysége a JAR fájl
* Csomag szinten definiálható a láthatóság
* JDK-t is modularizálták
  * Megszűnt az `rt.jar`, helyette a `jmods` könyvtárban 65< `.jmod` fájl
* Futásidőben is ellenőrzésre kerül
* Nem támogatja a dinamikus betöltést
* Jakarta EE 9.0 tér ki először a Java Platform Module Systemre
* Spring Framework esetén <br /> még meglehetősen körülményes a használata

---

## Könyvtárstruktúra

.small-code-14[
```plaintext
bookmarks
|   pom.xml
+---bookmarks-backend
|   |   pom.xml
|   \---src
|       \---main
|           \---java
|               |   module-info.java
|               |   
|               \---bookmarks
|                   \---backend
|                       |   Bookmark.java
|                       |   BookmarksService.java
|                           
\---bookmarks-frontend
    |   pom.xml
    \---src
        \---main
            \---java
                |   module-info.java
                \---bookmarks
                    \---frontend
                            BookmarksApplication.java                            
```
]

---

## module-info.java

```java
module bookmarks.app.backend {
    exports bookmarks.backend;
}
```

```java
module bookmarks.app.frontend {
    requires bookmarks.app.backend;
}
```

---

## maven-compiler-plugin

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
</plugin>
```

---

## Futtatás

.small-code-14[
```shell

java --module-path bookmarks-backend/target/classes;bookmarks-frontend/target/classes \
    --module bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication


java -p bookmarks-backend/target/classes;bookmarks-frontend/target/classes \
    -m bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication

java -p bookmarks-backend/target/bookmarks-backend-1.0-SNAPSHOT.jar; \
    bookmarks-frontend/target/bookmarks-frontend-1.0-SNAPSHOT.jar \
    -m bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication

java -p . -m bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication
```
]

---

## Hiba esetén

.small-code-14[
```plaintext
java.lang.module.FindException: Module bookmarks.app.backend not found, required by bookmarks.app.frontend

java.lang.module.FindException: Two versions of module bookmarks.app.backend 
found in . (bookmarks-backend-2.0-SNAPSHOT.jar and bookmarks-backend-1.0-SNAPSHOT.jar) 
```
]

---


## Modulok típusai

* System Modules: Java SE és JDK modulok
* Application Modules: module path-on, <br /> `module-info.class` fájllal rendelkező JAR-ok
* Automatic Modules: module path-on lévő JAR-ok, <br /> melyek nem rendelkeznek `module-info.class` fájllal. 
<br /> Nevük a JAR fájl nevéből származik. Module path-on van.
* Unnamed Module: classpath-on lévő osztályok kerülnek ide.

---

## További lehetőségek

* Requires Static: compile-time dependency
* Requires Transitive: a tranzitív függőséghez is lesz hozzáférése <br /> a függőség használójának
* Open/opens: megnyitás a reflection számára (pl. Hibernate, JAXB, Jackson)

---

class: inverse, center, middle

# Java Platform Module System - ServiceLoader mechanism

---

## ServiceLoader mechanism

* `bookmarks.backend.BookmarksService` interfész
* `bookmarks.backend.impl.InMemoryBookmarksService` implementáció

```java
module bookmarks.app.backend {
    exports bookmarks.backend;
 
    provides BookmarksService with InMemoryBookmarksService;
}
```

```java
module bookmarks.app.frontend {
    requires bookmarks.app.backend;

    uses BookmarksService;
}
```

---

## ServiceLoader használata

```java
private BookmarksService getBookmarksService() {
    return ServiceLoader.load(BookmarksService.class)
        .findFirst().orElseThrow(
            () -> new IllegalStateException("Service not found"));
}
```

---

class: inverse, center, middle

# Java Platform Module System - Java Linker

---

## Java Linker

```shell
jdeps --module-path . --module bookmarks.app.frontend

jlink --module-path "%JAVA_HOME%\jmods";. \
    --add-modules bookmarks.app.frontend --output bookmarks

bin\java -m bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication

jlink --module-path "%JAVA_HOME%\jmods";. \
    --add-modules bookmarks.app.frontend  \
    --launcher customjrelauncher=\
    bookmarks.app.frontend/bookmarks.frontend.BookmarksApplication  \
    --output bookmarks

bin\customjrelauncher.bat
```

* 292 MB helyett 39 MB

---

class: inverse, center, middle

# Collections Framework módosítások

---

## Kollekciók létrehozása

* `Set.of`, `List.of`, `Map.of`
* Nem csak varargs, hanem sok paraméteres - gyorsabb
* Null értéket nem lehet megadni
* Unmodifiable kollekciók
      * A módosító metódusok `UnsupportedOperationException` kivételt dobnak
      * Nem feltétlenül immutable (ha az elemei módosíthatóak)

---

## Kollekciók létrehozása példák

```java
List<String> names = List.of("John Doe", "Jack Doe", "Jane Doe");
// names.set(1, "Jack Jack Doe"); // UnsupportedOperationException
// names.add("John Smith"); // UnsupportedOperationException

List<String> nicks = Arrays.asList("John", "Little John", "Johnny");
nicks.set(1, "Little Little John"); // Valid
// nicks.add("JJ"); // UnsupportedOperationException
```

---

## Set és Map

```java
Map<Long, String> employees = Map.of(1L, "John Doe", 2L, "Jack Doe", 3L, "Jane Doe");
System.out.println(employees);
```

* Minden iterálásnál más sorrendben adja vissza a `Set` és `Map` az elemeket, <br /> hogy hamarabb
   kiderüljön, ha valami a sorrendre támaszkodik
* _Entry_ alapján is létrehozható:

```java
Map<Long, String> employees = Map.ofEntries(
        Map.entry(1L, "John Doe"),
        Map.entry(2L, "Jack Doe"),
        Map.entry(3L, "Jane Doe")
    );
```

---

## IndexOutOfBoundsException konstruktor

```java
public IndexOutOfBoundsException(int index) {
    super("Index out of range: " + index);
}
```

---

## Objects metódusok

```java
public static int checkFromToIndex(int fromIndex, int toIndex, int length) {
}

public static int checkFromIndexSize(int fromIndex, int size, int length) {
}
```

---

class: inverse, center, middle

# Stream API módosítások

---

## takeWhile

```java
IntStream.range(0, 10)
    .takeWhile(i -> i < 5)
    .mapToObj(i -> i)
    .collect(Collectors.toList());
// [0, 1, 2, 3, 4]
```

---

## dropWhile

```java
IntStream.range(0, 10)
    .dropWhile(i -> i < 5)
    .mapToObj(i -> i)
    .collect(Collectors.toList());
// [5, 6, 7, 8, 9]
```

---

## ofNullable

```java
List<String> names = Stream.ofNullable(name).collect(Collectors.toList());
// name = null esetén []
```

---

## iterate

```java
Stream.iterate(0, i -> i < 10, i -> i + 1)
    .collect(Collectors.toList());
// [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

* Második paraméter: `Predicate<? super T> hasNext`

---

## Optional.stream()

```java
List<Optional<Integer>> numbers = List.of(
        Optional.of(0),
        Optional.of(1),
        Optional.empty(),
        Optional.of(2),
        Optional.empty()
    );

numbers.stream()
    .flatMap(Optional::stream)
    .collect(Collectors.toList());
// [0, 1, 2]
```

---

## Optional.or()

```java
String out = Optional.ofNullable(name)
    .or(() -> Optional.of("anonymous")).get();
// name = null estén "anonymous"
```

---

## Optional.ifPresentOrElse()

```java
Optional.ofNullable(name)
    .ifPresentOrElse(System.out::println, () -> System.out.println("empty"));
// name = null esetén "empty"
```

---

class: inverse, center, middle

# IO módosítások

---

## Scanner metódusok

```java
public Stream<String> tokens() {
    // ...
}

public Stream<MatchResult> findAll(Pattern pattern) {
    // ...
}

public Stream<MatchResult> findAll(String patString) {
    // ...
}

```

---

## InputStream readAllBytes

```java
public byte[] readAllBytes() throws IOException {
    // ...
}

public int readNBytes(byte[] b, int off, int len) throws IOException {
    // ...
}
```

---

## InputStream transferTo

```java
public long transferTo(OutputStream out) throws IOException {
    Objects.requireNonNull(out, "out");
    long transferred = 0;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int read;
    while ((read = this.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
        out.write(buffer, 0, read);
        transferred += read;
    }
    return transferred;
}
```

---

class: inverse, center, middle

# VarHandle és MethodHandle

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

---

## Java 9 módosítások

* Ciklushoz és kivételkezeléshez hasonló MethodHandle combinators
* Fejlettebb paraméterkezelés
* Több lookup metódus (pl. nem abstract interfész metódusokhoz)

---

## VarHandle

* Alternatívát nyújt a `java.util.concurrent.atomic` és `sun.misc.Unsafe` használatához
* Típusos referencia egy változóhoz, melyet ezen keresztül olvasni és írni is lehet
* Segítségével atomi műveleteket lehet megvalósítani az attribútumokon <br /> az `atomic` osztályok használata nélkül

---

## Példa a lekérdezésre és atomi módosításra

```java
VarHandle vh = MethodHandles
    .privateLookupIn(Employee.class, MethodHandles.lookup())
    .findVarHandle(Employee.class, "name", String.class);
String name = (String) vh.get(employee);
```

```java
VarHandle vh = MethodHandles
    .privateLookupIn(Employee.class, MethodHandles.lookup())
    .findVarHandle(Employee.class, "age", int.class);
int age = (int) vh.getAndAdd(employee, 1);
```

---
class: inverse, center, middle

# Process API

---

## Process API

* `ProcessHandle` interfész
    * PID
* `Info` `Optional` attribútumokkal
    * Parancs elérési útja
    * Parancssor
    * Paraméterek
    * Indítás ideje
    * CPU idő
    * Felhasználó

---

## Folyamatok lekérdezése

* Aktuális folyamat lekérdezése: `ProcessHandle.current()`
* Összes folyamat lekérdezése: `ProcessHandle.allProcesses()` streamként

---

## Információk

```java
ProcessHandle
    .allProcesses()
    .map(processHandle -> processHandle.info())
    .map(ProcessHandle.Info::toString)
    .forEach(System.out::println);
```

* `isAlive()`, `supportsNormalTermination()` metódusok
* `children()`, `descendants()` streamként, `parent()`

---

## Folyamat megszűnés

* `destroy()`, `destroyForcibly()` metódusok
* `onExit()` `CompletableFuture` visszatérési értékkel

```java
ProcessHandle.allProcesses()
                .filter(p -> filterByName(p, "notepad"))
                .forEach(p -> p.onExit().thenApply((ph) -> {
                    System.out.println("Exited");
                    latch.countDown();
                    return ph;
                }));
```

---

class: inverse, center, middle

# CompletableFuture API továbbfejlesztés

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

---

## Új példánymetódusok

* `Executor defaultExecutor()` - Default Executort adja vissza, ez használt akkor, ha nincs explicit módon megadva Executor. Leszármazottban felülírható.
* `CompletableFuture<U> newIncompleteFuture()` - Ún. virtual constructor. Leszármazásnál hasznos, ha a leszármazott új példányt ad vissza, akkor annak típusának meghatározására.
* `CompletableFuture<T> copy()` - "Defensive copying", másolat készítése.
* `CompletionStage<T> minimalCompletionStage()` - Szintén másolat, de `CompletionStage` típusú.
* `CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor)`
* `CompletableFuture<T> completeAsync(Supplier<? extends T> supplier)` - Külön szálon futtatja a `Supplier`-t, és befejeződik, ha az befejeződik.
* `CompletableFuture<T> orTimeout(long timeout, TimeUnit unit)` - Kivételt dob, ha megadott időn belül nem fejeződik be.
* `CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit)` - A megadott értékkel sikeresen fejeződik be, ha a megadott időn belül nem fut le.

---

## Új statikus metódusok


* `Executor delayedExecutor(long delay, TimeUnit unit, Executor executor)`
* `Executor delayedExecutor(long delay, TimeUnit unit)` - Olyan executort ad vissza, mely a feladatokat 
  csak a megadott idő után kezdi el futtatni.
* `<U> CompletionStage<U> completedStage(U value)` - Olyan `CompletionStage` példányt ad vissza, mely már befejeződött a megadott értékkel.
* `<U> CompletionStage<U> failedStage(Throwable ex)`- Olyan `CompletionStage` példányt ad vissza, mely már befejeződött a megadott kivétellel.
* `<U> CompletableFuture<U> failedFuture(Throwable ex)` - Olyan `CompletableFuture` példányt ad vissza, mely már befejeződött a megadott kivétellel.

---

class: inverse, center, middle

# UTF-8 properties fájl

---

## Properties fájl beolvasás

```java
try {
    Properties properties = new Properties();
    properties.load(Props.class.getResourceAsStream("/messages.properties"));
    System.out.println(properties.get("message"));
} catch (IOException ioe) {
    // ...
}
```

```properties
message = árvíztűrő tükörfúrógép
```

```shell
native2ascii messages.properties messages.properties
```

* IDE: transparent native-to-ascii conversion

---

## Properties - Java 6

```java
try {
    Properties properties = new Properties();
    properties.load(new InputStreamReader(
        Props.class.getResourceAsStream("/messages.properties"), 
            StandardCharsets.UTF_8));
    System.out.println(properties.get("message"));
} catch (IOException ioe) {
    // ...
}
```

---

## ResourceBundle - Java 9

```java
try {
    ResourceBundle resourceBundle = new PropertyResourceBundle(
        Props.class.getResourceAsStream("/messages.properties"));
    System.out.println(resourceBundle.getString("message"));
} catch (IOException ioe) {
    // ...
}
```

---

class: inverse, center, middle

# Generated

---

# Generated annotáció

* Megjelent a `@Generated` annotáció (`javax.annotation.processing` csomagban)
* Generált kód megjelölésére, pl. MapStruct használja

Példák a használatára:

```java
@Generated("com.example.Generator")

@Generated(value="com.example.Generator", date= "2017-07-04T12:08:56.235-0700")

@Generated(value="com.example.Generator", date= "2017-07-04T12:08:56.235-0700",
   comments= "comment 1")
```

---

class: inverse, center, middle

# Deprecated

---

## Deprecated

* Deprecated interface elemei: `forRemoval` és `since`

```java
@Deprecated(since = "1.0.0", forRemoval = true)
public int getAge() {
    return age;
}
```

---

## Deprecated API-k

```java
@Deprecated(since="9")
public Boolean(boolean value) {
    this.value = value;
}
```

```java
@Deprecated(since="9")
protected void finalize() throws Throwable { }
```

---

## Applet API

* Applet API deprecated

---
class: inverse, center, middle

# Reactive Streams

---

## Reaktív Kiáltvány 

* [The Reactive Manifesto](https://www.reactivemanifesto.org/)
  * Reszponzivitás (Responsive): az alkalmazásnak minden esetben <br /> gyors választ kell adnia
  * Ellenállóképesség (Resilient): az alkalmazásnak reszponzívnak kell <br /> maradnia hiba esetén is
  * Elaszticitás (Elastic): reszponzivitás nagy terhelés esetén is
  * Üzenetvezéreltség (Message-driven): rendszerek elemei aszinkron, <br /> nem blokkoló módon, üzenetekkel kommunikálnak

---

## Reaktív manifesztó <br /> következményei

* Programozási paradigma, ahol a rendszer az adatelemek folyamára reagál
* Back pressure: reaktív nevezéktanban mechanizmus arra, <br /> hogy a termelő ne árassza el a fogyasztót
* Non-blocking back pressure: fogyasztó kéri a következő x elemet, <br /> amit fel tud dolgozni

---

## Reaktív megvalósítások <br /> Javaban

* Eclipse Vert.x
* Akka
* RxJava
* Project Reactor

---

## Reactive Streams

<img src="images/java-9-flow-api.png" alt="Reactive Streams" width="500">

---

## Tipikus megvalósítás

* Publisher, ahol létrejön az adat
* A `subscribe()` metódus hatására `Subscription` példányosítás és tárolás, <br /> `Subscriber.onSubscribe()` hívása
* A `Subscription.request()` metódus hívására <br /> `Subscriber.onNext()`, `onComplete()` vagy `onError()` hívás
* `Subscriber.onSubscribe()` hívásakor a `Subscription` eltárolása
* `Subscription.request()` hívása
* `Publisher.subscribe()` hívása egy `Subscriber`-rel
* Létező implementáció: `SubmissionPublisher`

---

## RxJava megvalósítás

```java
Flowable.fromIterable(employees)
                .filter(employee -> employee.getYearOfBirth() >= 2001)
                .map(Employee::getName)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
```

---

## Project Reactor megvalósítás

```java
Flux.fromIterable(employees)
                .filter(employee -> employee.getYearOfBirth() >= 2001)
                .map(Employee::getName)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
```

---

## Összehasonlításképp Java 8 implementáció

```java
employees
    .stream()
    .filter(employee -> employee.getYearOfBirth() >= 2001)
    .map(Employee::getName)
    .map(String::toUpperCase)
    .forEach(System.out::println);
```

---

## Frameworkök integrálása

```java
Flux.from(Flowable.fromIterable(employees)
                .filter(employee -> employee.getYearOfBirth() >= 2001)
                .map(Employee::getName))
                .map(String::toUpperCase)
                .subscribe(System.out::println);
```

Flux `from()` metódusa:

```java
public static <T> Flux<T> from(Publisher<? extends T> source) {
    // ...
}
```

---
class: inverse, center, middle

# StackWalking API

---

## StackWalking API

```java
StackWalker.getInstance()
                .forEach(System.out::println); // StackFrame
```

* `getClassName()`, `getMethodName()`, `getLineNumber()`, stb.

---

## walk

.small-code-14[
```java
public void m3() {
    List<String> methodNames = StackWalker.getInstance()
            .walk(this::collectShortMethodNames);
    System.out.println(methodNames);
}

private List<String> collectShortMethodNames(Stream<StackWalker.StackFrame> frames) {
    return frames
            .filter(frame -> frame.getMethodName().length() == 2)
            .map(frame -> frame.getMethodName())
            .collect(Collectors.toList());
}
```
]

---

## Option

```java
getInstance(Option.RETAIN_CLASS_REFERENCE)
```

* `RETAIN_CLASS_REFERENCE`: átadja a `Class` típusú hivatkozást, <br /> további reflection 
műveletek végrehajtásához
* `SHOW_REFLECT_FRAMES`: reflection frame-ek megtartása
* `SHOW_HIDDEN_FRAMES`: hidden frame-ek megtartása <br /> (pl. JVM belső/implementation specific) 

---
class: inverse, center, middle

# XML Catalog

---

## XSD hivatkozás

.small-code-14[
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<books xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns="https://www.training.com/schemas/books"
             xsi:schemaLocation="https://www.training.com/schemas/books 
                https://www.training.com/books.xsd">
    <book isbn10="059610149X">
        <title>Java and XML</title>
    </book>
</books>
```
]

---

## XSD validáció DOM-mal

.small-code-14[
```java
final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";

DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
factory.setNamespaceAware(true);
factory.setValidating(true);
DocumentBuilder documentBuilder = factory.newDocumentBuilder();

documentBuilder.setErrorHandler(new ErrorHandler() {
    // ... metódusok implementációja, mely kivételt dob
});
Document document = documentBuilder.parse(XmlDemo.class.getResourceAsStream("/books.xml"));
```
]

```plaintext
Caused by: java.io.FileNotFoundException: 
https://www.training.com/books.xsd
```

---

## Java 9 Catalog API

```java
URI catalogUri = 
    XmlDemo.class.getResource("/catalog.xml").toURI();
CatalogResolver cr = CatalogManager.catalogResolver(
    CatalogFeatures.defaults(), catalogUri);
documentBuilder.setEntityResolver(cr);
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
    <system systemId="https://www.training.com/books.xsd" uri="books.xsd"/>
</catalog>
```

---
class: inverse, center, middle

# JVM módosítások

---

## Szemétgyűjtő

* G1 az alapértelmezett szemétgyűjtő

---

## Compact String

* Amennyiben egy String karakterei ábrázolhatóak a Latin-1 karakterkódolással, <br />
  nem az alapértelmezett 2 byte-on, hanem 1 byte-on tárolja a karaktereket
* Belső implementáció: egy `coder` mező értéke alapján <br /> vagy a `byte[]` vagy a `char[]` attribútumot használja

---

## Egységes JVM naplózás

```shell
java -Xlog:help log.Input
java -Xlog:gc=debug:class:stdout -Xlog:class=debug:class:stdout log.Input
java -Xlog:gc=debug
java -Xlog:class+load=debug,gc=debug
java -Xlog:gc=debug:file=vm.log
```

---
class: inverse, center, middle

# Tool frissítések

---

## Ahead-of-Time Compilation

* Fordítási időben natív kóddá fordítás Graal compilerrel
* 17-es Javaban eltávolították, mert nem használták, és <br />
  költséges volt a karbantartása
* Használható helyette a GraalVM, mely képes natívra is fordítani
* Speciális felhasználási terület: AWS Lambda
  * Serverless compute service
  * Infrastruktúra felügyelete nem szükséges
  * Kódfuttatás - rövid életű lambda function
  * Csak a felhasznált erőforrást kell kifizetni

---

## JavaDoc

* HTML5 támogatás
* Kereshetőség a JavaDoc-ban

---

## Eltávolított eszköz

* JavaDB: Apache Derby inmemory adatbázis a JDK-n belüli disztribúció

---
class: inverse, center, middle

# JShell

---

## JShell

* REPL
* History, code complete, open/save

```shell
jshell

jshell> System.out.println("Hello JShell");
Hello JShell

jshell> int x = 5;
x ==> 5

jshell> int y = x + 6;
y ==> 11

jshell> /list

   1 : System.out.println("Hello JShell");
   2 : int x = 5;
   3 : int y = x + 6;

jshell> /exit
```