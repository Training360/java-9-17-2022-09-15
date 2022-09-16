class: inverse, center, middle

# Újdonságok Java 14-től

* Switch Expressions (Standard) - JEP 361
* `@Serial` annotáció
* `PrintStream` változtatások
* Helpful NullPointerExceptions - JEP 358
* CMS GC törlése - JEP 363
* Deprecate the ParallelScavenge + SerialOld GC Combination - JEP 366
* Remove the Pack200 Tools and API - JEP 367

## Preview, incubator

* Pattern Matching for instanceof (Preview) - Java 16-ban végleges - JEP 305
* Records (Preview) - Java 16-ban végeleges - JEP 359
* Text Blocks (Second Preview) - Java 15-ben végleges - JEP 368
* Foreign-Memory Access API (Incubator) - JEP 370
* ZGC (csak Java 15-ben végleges) Mac és Windows operációs rendszereken - JEP 364, 365
* Packaging Tool (Incubator) - Java 16-ban végleges - JEP 343

## Switch expressions

```java
switch (fruit) {
        case APPLE, PEAR -> System.out.println("Common fruit");
        case ORANGE, AVOCADO -> System.out.println("Exotic fruit");
        default -> System.out.println("Undefined fruit");
    }
```

* Fall-through mechanizmus megakadályozása

```java
String text = switch (fruit) {
        case APPLE, PEAR -> "Common fruit";
        case ORANGE, AVOCADO -> "Exotic fruit";
        default -> "Undefined fruit";
    };
```

```java
String text = switch (fruit) {
        case APPLE, PEAR -> {
            System.out.println("the given fruit was: " + fruit);
            yield "Common fruit";
        }
        case ORANGE, AVOCADO -> "Exotic fruit";
        default -> "Undefined fruit";
    };
```

* `yield` kulcsszó

Régi formátummal is működik:

```java
switch (fruit) {
        case APPLE, PEAR:
            yield "Common fruit";
        case ORANGE, AVOCADO:
            yield "Exotic fruit";
        default:
            yield "Undefined fruit";
    }
```

## @Serial annotáció

A szerializáció Javaban: 

* `Serializable` interfészt implementálja, de nem ebben vannak a metódusok
* Speciális attribútumok: `serialPersistentFields`, `serialVersionUID`
* Speciális metódusok: `writeObject()`, `readObject()`, `readObjectNoData()`, `writeReplace()`, `readResolve()`
* Ezeket lehet megjelölni, hogy a fordítóprogram ellenőrizni tudja

## PrintStream változtatások

* `PrintStream.write(byte[])`, `PrintStream.writeBytes(byte[])`
* Előbbi deklarál ugyan, de sosem dob `IOException` kivételt
* Utóbbi nem deklarál kivételt

## Helpful NullPointerExceptions

* Láncolt metódushívás esetén nem tudtuk mi volt null

```plaintext
Exception in thread "main" java.lang.NullPointerException
```

```plaintext
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "GrapeClass.getColor()" because the return value of "java.util.HashMap.get(Object)" is null
```

