class: inverse, center, middle

# Újdonságok Java 17-től

---

* Kiadás: 2021. szeptember
* LTS

---

* Sealed Classes - JEP 409
* `Stream.toList()`
* Console Charset API (JDK-8264208) - `Console.charset()`
* `native.encoding` system property
* Enhanced Pseudo-Random Number Generators, `java.util.random` csomag - JEP 356
* `HexFormat` class
* `Process.errorReader()`, `Process.inputReader()`, `Process.outputWriter()`
* `Map.Entry.copyOf()`
* Strongly Encapsulate JDK Internals - JEP 403 (JEP 396 folytatása)
* Deprecate the Applet API for Removal - JEP 398
* Remove RMI Activation - JEP 407
* Deprecate the Security Manager for Removal - JEP 411
* Restore Always-Strict Floating-Point Semantics - JEP 306
* Remove the Experimental AOT and JIT Compiler - JEP 410
* JavaDoc összehasonlítja a verziókat (https://docs.oracle.com/en/java/javase/17/docs/api/new-list.html),
 és továbbfejlesztett Deprecated oldal (https://docs.oracle.com/en/java/javase/17/docs/api/deprecated-list.html)

* Context-Specific Deserialization Filters - JEP 415

## Preview, incubator

* JEP 406: Pattern Matching for switch (Preview)
* Vector API (Second Incubator) - JEP 414
* Foreign Function & Memory API (Incubator) - JEP 412

## Sealed Classes

* Jobb vezérlés, hogy ki származhat le
* API fejlesztőknek

```java
public abstract sealed class FruitSealed permits AppleSealed, PearSealed {
}
public non-sealed class AppleSealed extends FruitSealed {
}
public final class PearSealed extends FruitSealed {
}
```


```java
public sealed interface ConstantDesc
        permits ClassDesc,
                MethodHandleDesc,
                MethodTypeDesc,
                Double,
                DynamicConstantDesc,
                Float,
                Integer,
                Long,
                String {
                        // ...
                }                        
```

## Stream.toList()

```java
List<String> names = employees.map(Employee::getName).collect(Collectors.toList());
```

```java
List<String> names = employees.map(Employee::getName).toList();
```

## Console

```java
System.out.println(System.console().charset());
System.out.println(System.getProperty("native.encoding"));
```

* Első sor IDEA esetén `NullPointerException`, mert a `System.console()` értéke `null`

Parancssorból:

```plaintext
IBM852
Cp1250 
```

## Enhanced Pseudo-Random Number Generators

```java
RandomGeneratorFactory.all()
                .map(fac -> fac.group()+":"+fac.name()
                        + " {"
                        + (fac.isSplittable()?" splitable":"")
                        + (fac.isStreamable()?" streamable":"")
                        + (fac.isJumpable()?" jumpable":"")
                        + (fac.isArbitrarilyJumpable()?" arbitrary-jumpable":"")
                        + (fac.isLeapable()?" leapable":"")
                        + (fac.isHardware()?" hardware":"")
                        + (fac.isStatistical()?" statistical":"")
                        + (fac.isStochastic()?" stochastic":"")
                        + " stateBits: "+fac.stateBits()
                        + " }"
                )
                .sorted().forEach(System.out::println);
```


```java
RandomGenerator generator = RandomGeneratorFactory.of("Legacy:Random").create();
```

* Nem thread-safe
* `create()` metódusnak paraméterül átadható seed is
* `ints()`, `longs()`, `doubles()` metódusok
* `nextInt()`, `nextInt(bound)`, `nextInt(origin, bound)`

## Változott Random osztály

* `implements RandomGenerator`
* `@RandomGeneratorProperties`
* Sok default metódus

```java
default int nextInt(int origin, int bound) {
    RandomSupport.checkRange(origin, bound);

    return RandomSupport.boundedNextInt(this, origin, bound);
}
```