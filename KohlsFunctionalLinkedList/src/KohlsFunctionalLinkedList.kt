package Funktional

import java.lang.IllegalStateException

// Eine nicht veränderbare Liste

//seald verhindert weitere ableitungen ausserhalb von dieser klasse

//es gibt nur diesen beiden ableitungen NIL und NODE
sealed class List<T> {

    //6:40 funktional verk liste erwzingen der unterklasse zur implementierung
    abstract fun isEmpty() : Boolean


    // vom stil her objektorient gedacht
    //realiserung der verzwigung über Polymorphie lösen. Beide Typen werden gleich beahndelt aber zur Laufzeit wird dynamisch entschieden
    //was azsgeführt wwird
    object Nil : List<Nothing>(){
        override fun isEmpty(): Boolean =  true
    }
    class Node<T> (val head : T , val tail : List<T>) : List<T>() {
        override fun isEmpty(): Boolean = false
    }


    //Idiomatisch für die Funktionale Programmierung = diese fall unterscheidung selber machen
    //funktionales Paradigma
    fun istLeer() : Boolean = when(this){
        is Nil -> true
        is Node<T> -> false
    }//when deckt alle möglichkeiten ab da es sich um eine sealed klasse handelt.


    override fun toString() : String = when (this){
        is Nil -> "NIL"
        is Node -> "$head, $tail"
    }


    fun addFirst(data: T ) : List<T> = Node(data, this)

    //neuer knoten der auf den restlichen tail verweisst
    fun setHead(data : T) : List<T> = when (this){
        is Nil -> throw IllegalStateException("empty list has no head")
        is Node -> Node (data, this.tail)
    }

    fun removeFirst() : List<T> = when (this){
        is Nil -> throw IllegalStateException("head cannot be removed from empty list")
        is Node -> tail
    }

    // map soll eine lsite ikn eine andere überführen
// die werte T werden überführt in R
    //map und fold erklärung 8:37
    fun <R> map  ( f: (T) -> R ) : List<R> = when(this){
        is Nil -> Nil as List<R>
        is Node -> Node( f(head), tail.map(f))
    }
    // 20:00
    fun <R> fold (accumulated : R, f : (T, R) -> R) : R = when (this) {
        is Nil -> accumulated
        is Node<T> -> tail.fold( f(head, accumulated) , f)
    }



    //REKURSIV UND ENDREKUSIV IST NICHT DAS SELBE

    //Endrekursive Funktionen 7:00
    fun forEach(f : (T) -> Unit ){
        if (this is Node){
            f(head)
            tail.forEach(f)
        }
    }
    //Koltin kann funktonen verschachteln dies ist tatsächlich ein endrukiver aufruf davor war es keiner den
    // die list an sich wurde nicht genannt 11:00. Wei ldie FUnktion selber nicht aufgerufen wird sonder der tail.
    fun fuerAlle (f: (T) -> Unit){
        tailrec fun fuerAlle (f: (T) -> Unit, list: List<T>){
            if (list is Node){
                f(list.head)
                fuerAlle(f, list.tail)
            }
        }

        fuerAlle(f, this)
    }



    //statische funktion campianion obejekt
    companion object {
        fun <T> createEmpytList() : List<T> = Nil as List<T>

        //operatoren können überladen werden. KOTLIN Verkettete Liste - Grundlegende Funktionen 12:15
        operator

        fun <T> invoke ( vararg values : T ) : List<T>{
            val empty = createEmpytList<T>() //typ muss nicht angegeben werden kotlin inferiert selber Verkettete Liste - Grundlegende Funktionen 17:23 erlärung
            val res = values.foldRight(empty, { data, l -> l.addFirst(data)})
            return res
        }
    }
//bis heir hin waren basis fnktionen jetzt kommen krasse. es waren funktoinen wie aus ap2 jetzt map usw ab ca zeile 54
// was hier neu ist der umstand dass werte in der lsite nicht verändert werden können.
// eine unveränderliche Liste. und auf basis dieser lsite erzeugen wir  neue listen.
// vorteil der funktionalen programmierung sind funktionen höherer ordnung. diese können mit weiter funktionen parametisiert werden.







}




