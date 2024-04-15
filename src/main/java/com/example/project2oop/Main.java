package com.example.project2oop;

public class Main {
    public static void main(String[] args) {
        // 2.1.1 Creating an instance of SubClass & 2.1.7
        SubClass subClass = new SubClass(10);

        // 2.1.2 Demonstrating possibility to create instance of subclass while declaring it as of type of superclass
        SuperClass a =  new SubClass(); // We can assign a subclass object to a variable declared with the superclass type due to polymorphism
        System.out.println("Class of 'a' instance: " + a.getClass()); // 2.1.2 Print result of method getClass() into console

        // 2.1.3 Demonstrate ability to access superclass instance variables inside the child class methods.
        subClass.accessSuperClassVarFromChild();

        // 2.1.4 Write and call overloaded methods.
        a.overloadingMethod();
        subClass.overloadingMethod("Hello");
        subClass.overloadingMethod("Hello, World", 3);
        subClass.display();
        a.display();

        // 2.1.6 Use of super keyword. Demonstrate ability to call overridden method inside the overriding method using super keyword.
        subClass.printMethod();

        // 2.1.8 Demonstrate abstract classes and methods.
        a.abstractMethod();
    }
}

