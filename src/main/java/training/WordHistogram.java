package training;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class WordHistogram {

    public static void main(String[] args) {
        var text = """
            A streamek a Java nyelvben adatfolyamok. Úgy kell elképzelni, mint egy futószalagot, 
            ahol sorban jönnek az adatok, és minden művelet csinál velük valamit. A közbenső műveletek (intermediate operations) 
            eredménye szintén stream, így folytathatjuk a feldolgozást, míg a záró művelet (terminal operation) kimenete 
            valamilyen kollekció vagy más objektum. A feldolgozás ún. lusta kiértékelésű (lazy evaluation), azaz a műveletek 
            csak akkor hajtódnak végre, amikor szükség van rájuk, és csak azokon az adatokon, amelyeken feltétlenül szükséges. 
            Az egész feldolgozást a záró művelet indítja el. Enélkül nem történik semmi, az adatok a futószalag előtt várakoznak 
            feldolgozásra.""";

        // Írjuk ki előfordulás sorrendjében hogy melyik betű hányszor szerepel

        var result = text.chars()
                .mapToObj(i -> (char) i) // CharStream NINCS
                .filter(Character::isAlphabetic)
                .map(Character::toLowerCase)
                .collect(Collectors.groupingBy(
                        c -> c,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry<Character, Long>::getValue).reversed())
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(", "));

        System.out.println(result);

    }
}
