# LFA

## AFN - Proiectul 1

### Cum scriu AFN-ul in fisier?

1. Prima linie e numarul de tranzitii (n)
2. Urmatoarele n linii sunt ocupate de cele n tranzitii in urmatorul format
    * Stare_de_plecare	->	Simbol_citit	->	Starea_in_care_ajung
3. Prima stare din cele din fisier e starea initiala prin conventie
4. Orice stare finala va fi marcata cu @ (ex: q0 a @q1) si vom pune @ doar la starile in care ajungem (doar ele fiind relevante). De asemenea, starile fiind memorate sub forma de set, daca o stare finala apare de mai multe ori in definirea automatului, ea poate fi marcata doar o singura data ca fiind finala.
5. **!!!Momentan starile pot fi retinute doar printr-un singur caracter, cum ar fi 0 sau s. Notatiile de tipul q0, q0124 nu merg momentan.!!!**

### Unde e fisierul?

`LFA/AFN/cmake-build-debug/afn.in`
