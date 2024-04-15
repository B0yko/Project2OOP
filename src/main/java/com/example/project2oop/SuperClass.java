package com.example.project2oop;

// 2.1.8 Demonstrate abstract classes and methods.
abstract class abstractClass {
    abstract void abstractMethod();
}

// 2.1.1 Creating a SuperClass
public class SuperClass extends abstractClass{
    // Creating instance variables for superclass with different access modifiers
    private int privateN = 5;
    protected  int protectedN = 15;
    public int publicN = 30;
    int getPrivateN() {
        return privateN;
    }
    // 2.1.4 Write overloaded methods.
    void overloadingMethod() {
        System.out.println("Empty overloadingMethod");
    }
    void overloadingMethod(String message) {
        System.out.println("Message: " + message);
    }
    // 2.1.5 Write and call overridden methods.
    void display() {
        System.out.println("SuperClass method");
    }
    // 2.1.6 Use of super keyword.
    void printMethod() {
        System.out.println("Output from SuperClass");
    }
    // 2.1.7 Use of super keyword for parent constructor call
    SuperClass() {
        System.out.println("Default SuperClass constructor");
    }
    SuperClass(int n) {
        System.out.println("Superclass constructor with value: " + n);
    }

    // 2.1.8 Demonstrate abstract classes and methods.
    @Override
    void abstractMethod() {
        System.out.println("Implementation of abstractMethod() in SuperClass");
    }
}
