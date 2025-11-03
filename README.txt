Smells Removed:

    God Class split into focused services

    Primitive Obsession replaced with strategy patterns

    Global State eliminated via dependency injection

    Shotgun Surgery prevented via centralized pricing logic

Refactorings Applied:

    Extract Class for policies and services

    Replace Conditional with Polymorphism

SOLID Principles:

    Single Responsibility through focused classes

    Open/Closed via extensible interfaces

Extensibility:
New discounts implement DiscountPolicy interface
No existing code modifications required

Why is adapting the legacy printer better than changing your domain or vendor class?

Because we have two systems, one is our domain system (use String-based printing), another one is vendor system (use byte[]).
Changing domain class would pollute clean, modern code with old technical details. Changing vendor class would break other clients who's using it, and we don't own vendor's code.
So we introduce adapter to transfer between the two.
