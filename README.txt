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


Trade-offs: Layering vs Partitioning

• Why did you choose a Layered Monolith (for now) rather than partitioning into multiple services?
The current scope of the Café POS & Delivery system is small and tightly integrated. By not starting with microservices, we avoid the complexity of distributed systems.
Since the module is about learning design patterns, a layered monolith allows us to concentrate on clean architecture, cohesion, and maintainability.
It also simplifies testing and development, enabling faster iteration while the domain model is still evolving.
